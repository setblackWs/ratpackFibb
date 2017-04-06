package pl.setblack.fibbo;

import org.junit.jupiter.api.Test;
import ratpack.http.client.HttpClient;
import ratpack.test.embed.EmbeddedApp;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by jarek on 2/10/17.
 */
class FibboServerTest {


    @Test
    public void testFibbo() throws Exception{
        final HttpClient httpClient = HttpClient.of(rs->rs.readTimeout(Duration.ofMinutes(2)));

        EmbeddedApp.fromServer(
                FibboServer.createServer(httpClient)
        ).test( testHttpClient -> {
            assertEquals("5", testHttpClient.get("fibbo/4").getBody().getText());
        });
    }
}