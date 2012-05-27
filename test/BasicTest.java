import models.FullRepository;
import models.LightRepository;

import org.junit.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

import play.libs.F.Promise;
import play.libs.WS.HttpResponse;
import play.test.*;
import utils.Page;

public class BasicTest extends UnitTest {

    @Test
    public void searchLightRepository() throws Exception {
    	Page<LightRepository> page = LightRepository.getPage("homework", 1);
    	assertNotNull(page);
    	assertNotNull(page.data);
    	assertEquals(LightRepository.PAGE_SIZE, page.data.size());

    	LightRepository repo = page.data.get(0);
    	assertEquals("homework", repo.name);
    	assertEquals("homework", repo.username);
    	assertEquals("Openflow/NoX Homework router implementation", repo.description);
    }

    @Test
    public void getFullRepository() throws Exception {
    	FullRepository repo = FullRepository.get("playframework", "play20");
    	assertNotNull(repo);
    	assertNotNull(repo.users);
    	assertFalse(repo.users.isEmpty());
    }
}
