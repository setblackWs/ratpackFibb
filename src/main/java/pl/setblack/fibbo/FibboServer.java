package pl.setblack.fibbo;

import ratpack.server.RatpackServer;

/**
 * Created by jarek on 2/9/17.
 */
public class FibboServer {
    public static void main(String... args) throws Exception {
        RatpackServer.start(server -> server
                .handlers(chain -> chain
                        .prefix("fibbo", fibbo
                                -> fibbo.get(":n",
                                ctx -> ctx.render("Hello " + ctx.getPathTokens().get("n") + "!")))
                )
        );
    }
}
