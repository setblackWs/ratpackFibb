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
                                serverConfigBuilder.threads(1)

                        )
                        .handlers(
                                chain -> chain.prefix("fibbo", fibbo -> fibbo.get(":n", fibbHandler()))));

    }

    private static Handler fibbHandler() throws Exception{
        final HttpClient httpClient = HttpClient.of(rs->rs.readTimeout(Duration.ofMinutes(2)));

        return ctx -> {
            final Long n = Long.parseLong(ctx.getPathTokens().get("n"));

            ctx.render(String.valueOf(n));

        };
    }


}
