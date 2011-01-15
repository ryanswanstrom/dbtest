
import java.util.HashMap;
import java.util.Map;
import org.junit.*;
import play.test.*;
import play.mvc.Http.*;

public class ApplicationTest extends FunctionalTest {

    @Test
    public void testThatIndexPageWorks() {
        Response response = GET("/");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset("utf-8", response);
    }

    @Test
    public void testThatResultsWorks() {
        Response response = GET("/speedResults/1200/8");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset("utf-8", response);
    }

    @Test
    public void testSpeedWorks() {
        Map map = new HashMap();
        map.put("iters", "5");
        Response response = POST("/application/speedTest", map, new HashMap());
        assertStatus(302, response);
        System.out.println("headers " + response.headers);
        String redir = response.headers.get("Location").value().substring(16); // remove http://localhost
        System.out.println("redirect : " + redir);
        response = GET(redir);
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset("utf-8", response);
    }
}
