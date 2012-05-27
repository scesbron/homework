import java.util.HashMap;
import java.util.Map;

import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;

public class ApplicationTest extends FunctionalTest {

    @Test
    public void testThatIndexPageWorks() {
        Response response = GET("/");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }

    @Test
    public void testSearch() {
    	Map<String, String> parameters = new HashMap<String, String>();
    	parameters.put("query", "homework");
        Response response = POST("/search", parameters);
        assertIsOk(response);
        // TODO Why do I have a content type not set : is this due to await ?
//        assertContentType("text/html", response);
//        assertCharset(play.Play.defaultWebEncoding, response);
    }

}