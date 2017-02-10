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

    private static Handler fibbHandler() {
        return ctx -> {
            System.out.println("me waiting");
            Blocking.exec(
                    ()->{
                        Thread.sleep(4000);
                        System.out.println("was waiting");
                        ctx.render("7");
                    }
            );

        };
    }


}
