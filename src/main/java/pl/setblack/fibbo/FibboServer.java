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

        return RatpackServer.start(server -> server
                .serverConfig(cfg ->
                        cfg
                                .development(false)
                                .threads(1)
                                .connectTimeoutMillis(60 * 1000))
                .handlers(chain -> chain
                        .prefix("fibbo", fibbo
                                -> fibbo.get(":n",
                                ctx ->ctx.render("hello!")))));

    }

}
