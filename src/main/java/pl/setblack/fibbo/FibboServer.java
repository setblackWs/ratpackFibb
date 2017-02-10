package pl.setblack.fibbo;

import ratpack.exec.Blocking;
import ratpack.exec.Promise;
import ratpack.handling.Handler;
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
        RatpackServer.start(ratpackServerSpec ->
                ratpackServerSpec
                        .serverConfig(serverConfigBuilder ->
                                serverConfigBuilder.threads(4)

                        )
                        .handlers(
                                chain -> chain.prefix("fibbo", fibbo -> fibbo.get(":n", fibbHandler()))));

    }

    private static Handler fibbHandler() throws Exception{
        final HttpClient httpClient = HttpClient.of(rs->rs.readTimeout(Duration.ofMinutes(2)));

        return ctx -> {
            final Long n = Long.parseLong(ctx.getPathTokens().get("n"));
            //System.out.println("Fibb ("+n+")   t:"+Thread.activeCount());
            if ( n < 2 ) {
                ctx.render(String.valueOf(1));
            } else {
                 Promise<Long> fibbn_1 = httpClient.get(new URI("http://localhost:5050/fibbo/"+(n-1)))
                         .map( resp -> Long.parseLong(resp.getBody().getText()));
                Promise<Long> fibbn_2 = httpClient.get(new URI("http://localhost:5050/fibbo/"+(n-2)))
                        .map( resp -> Long.parseLong(resp.getBody().getText()));
                fibbn_1.then( f1Val -> fibbn_2.then(f2Val -> ctx.render(String.valueOf(f1Val + f2Val))));

            }

        };
    }



}
