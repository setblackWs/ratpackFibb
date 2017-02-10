package pl.setblack.fibbo;

import ratpack.exec.Blocking;
import ratpack.exec.Promise;
import ratpack.http.client.HttpClient;
import ratpack.server.RatpackServer;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

/**
 * Created by jarek on 2/9/17.
 */
public class FibboServer {

    public static void main(String... args) throws Exception {
        final HttpClient httpClient = HttpClient.of(rs->rs.readTimeout(Duration.ofMinutes(2)));

        createServer(httpClient);
    }

    public static RatpackServer createServer(HttpClient httpClient) throws Exception {
        return RatpackServer.start(server -> server
                .serverConfig(cfg->
                        cfg
                                .development(false)
                                .threads(10)
                                .connectTimeoutMillis(60*1000))
                .handlers(chain -> chain
                        .prefix("fibbo", fibbo
                                -> fibbo.get(":n",
                                ctx -> {
                                    /*Blocking.exec(()->*/{
                                        long n = Long.parseLong(ctx.getPathTokens().get("n"));
                                        System.out.println("fibb ("+n+") Threads:" + Thread.activeCount());
                                        Thread.sleep(1000);
                                        if ( n < 2 ) {
                                            ctx.render("1");
                                        } else {
//                                       //HttpClient httpClient = ctx.get(HttpClient.class);
                                            Promise<Long> result1 = requestFibb(n-1, httpClient);
                                            Promise<Long> result2 = requestFibb(n-2, httpClient);

                                            result1.then( v1 -> result2.then(
                                                    v2-> ctx.render(String.valueOf(v1+v2))
                                            ));


                                        }
                                    }


                                }
                ))
        ));
    }

    private static Promise<Long> requestFibb(long n, HttpClient httpClient) throws URISyntaxException {
        try {

            return httpClient.get(new URI("http://localhost:5050/fibbo/"+ n))
                    .map(resp -> Long.parseLong(resp.getBody().getText()));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
