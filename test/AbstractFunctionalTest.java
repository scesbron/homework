import play.mvc.Http.Response;
import play.test.FunctionalTest;


public abstract class AbstractFunctionalTest extends FunctionalTest {

	public Response async(Response response) {
		while (response.status == 200 && "".equals(response.out.toString())) {
			sleep(1);
		}
		return response;
	}

}
