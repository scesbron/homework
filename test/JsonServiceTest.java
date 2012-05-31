import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.*;
import play.test.*;
import play.i18n.Messages;
import play.mvc.*;
import play.mvc.Http.*;

public class JsonServiceTest extends AbstractFunctionalTest {
	@Test
	public void testSearch() {
		Response response = async(GET("/json/search/homework"));
		assertIsOk(response);
		assertContentType("application/json", response);
		assertCharset(play.Play.defaultWebEncoding, response);
	}

	@Test
	public void testShow() {
		Response response = async(GET("/json/show/playframework/play20"));
		assertIsOk(response);
		assertContentType("application/json", response);
		assertCharset(play.Play.defaultWebEncoding, response);
	}
}