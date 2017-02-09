package pl.setblack.fibbo;

import ratpack.exec.Promise;
import ratpack.http.client.HttpClient;
import ratpack.server.RatpackServer;

import java.net.URI;

/**
 * Created by jarek on 2/9/17.
 */
public class FibboServer {
    public static void main(String... args) throws Exception {
        RatpackServer.start(server -> server
                .serverConfig(cfg->cfg.threads(1))
                .handlers(chain -> chain
                        .prefix("fibbo", fibbo
                                -> fibbo.get(":n",
                                ctx -> {
                                    long n = Long.parseLong(ctx.getPathTokens().get("n"));
                                    System.out.println("fibb ("+n+") Threads:" + Thread.activeCount());
                                    if ( n < 2 ) {
                                        ctx.render("1");
                                    } else {
                                        HttpClient httpClient = ctx.get(HttpClient.class);
                                        Promise<Long> result1 = httpClient.get(new URI("http://localhost:5050/fibbo/"+ (n-1)))
                                                .map(resp -> Long.parseLong(resp.getBody().getText()));
                                        Promise<Long> result2 = httpClient.get(new URI("http://localhost:5050/fibbo/"+ (n-2)))
                                                .map(resp -> Long.parseLong(resp.getBody().getText()));
                                        result1.then( v1 -> result2.then(
                                                v2-> ctx.render(String.valueOf(v1+v2))
                                        ));


                                    }

                                }
                ))
        ));
    }
}
