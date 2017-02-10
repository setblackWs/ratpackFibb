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
        RatpackServer.start(ratpackServerSpec ->
                ratpackServerSpec.handlers(
                        chain -> chain.prefix("fibbo", fibbo -> fibbo.get(":n", ctx -> ctx.render("7")))));

    }




}
