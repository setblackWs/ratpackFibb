package pl.setblack.fibbo;

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
        createServer();

    }

    public static RatpackServer createServer() throws Exception {
        final HttpClient httpClient = HttpClient.of(rs -> rs.readTimeout(Duration.ofMinutes(2)));


        return RatpackServer.start(server -> server
                .serverConfig(cfg ->
                        cfg
                                .development(false)
                                .threads(1)
                                .connectTimeoutMillis(60 * 1000))
                .handlers(chain -> chain
                        .prefix("fibbo", fibbo
                                -> fibbo.get(":n",
                                ctx -> {
                                    long n = Long.parseLong(ctx.getPathTokens().get("n"));
                                    if (n <= 2) {
                                        ctx.render(String.valueOf(n));
                                    } else {
                                        Promise<Long> n1 = requestFibb(n - 1, httpClient);
                                        Promise<Long> n2 = requestFibb(n - 2, httpClient);
                                        Promise<Long> resultLong = n1.flatMap(x -> n2.map(y -> y + x));
                                        Promise<String> result = resultLong.map(r -> String.valueOf(r));
                                        ctx.render(result);
                                    }
                                }
                        ))));
    }

    private static Promise<Long> requestFibb(long n, HttpClient httpClient) throws URISyntaxException {
        try {

            return httpClient.get(new URI("http://localhost:5050/fibbo/" + n))
                    .map(resp -> Long.parseLong(resp.getBody().getText()));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
