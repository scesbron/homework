import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.*;
import play.test.*;
import play.i18n.Messages;
import play.mvc.*;
import play.mvc.Http.*;

public class ApplicationTest extends AbstractFunctionalTest {

	@Test
	public void testThatIndexPageWorks() {
		Response response = async(GET("/"));
		assertIsOk(response);
		assertContentType("text/html", response);
		assertCharset(play.Play.defaultWebEncoding, response);
	}

	@Test
	public void testSearch() {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("query", "homework");
		Response response = async(POST("/search", parameters));
		assertIsOk(response);
		assertContentType("text/html", response);
		assertCharset(play.Play.defaultWebEncoding, response);
		assertContainsMessage(response, "index.title");
		assertContainsMessage(response, "table.explanation", 1, 100);
	}

	@Test
	public void testSearchOneResult() {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("query", "scesbron");
		Response response = async(POST("/search", parameters));
		assertStatus(StatusCode.FOUND, response);
		// Do the test like this because with async the redirection value is not
		// correct
		assertTrue(response.headers.get("Location").value().endsWith("/scesbron/homework"));
	}

	@Test
	public void testShow() {
		Response response = async(GET("/playframework/play20"));
		assertIsOk(response);
		assertContentType("text/html", response);
		assertCharset(play.Play.defaultWebEncoding, response);
		assertNotContainsMessage(response, "show.sorry");
		assertContainsMessage(response, "show.title", "playframework", "play20");
	}

	/**
	 * Asserts response body matched a pattern or contains some text.
	 *
	 * @param pattern
	 *            a regular expression pattern or a regular text, ( which must
	 *            be escaped using Pattern.quote)
	 * @param response
	 *            server response
	 */
	public void assertContentNotMatch(String pattern, Response response) {
		Pattern ptn = Pattern.compile(pattern);
		boolean ok = ptn.matcher(getContent(response)).find();
		assertFalse("Response content match '" + pattern + "'", ok);
	}

	public void assertContains(String text, Response response) {
		assertContentMatch(".*" + text + ".*", response);
	}

	public void assertNotContains(String text, Response response) {
		assertContentNotMatch(".*" + text + ".*", response);
	}

	public void assertContainsMessage(Response response, String text, Object... args) {
		assertContentMatch(".*" + Messages.get(text, args) + ".*", response);
	}

	public void assertNotContainsMessage(Response response, String text, Object... args) {
		assertContentNotMatch(".*" + Messages.get(text, args) + ".*", response);
	}
}