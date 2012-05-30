import models.Commit;
import models.FullRepository;
import models.LightRepository;
import models.User;

import org.junit.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

import jobs.GetFullRepository;

import play.cache.Cache;
import play.libs.F.Promise;
import play.libs.WS.HttpResponse;
import play.test.*;
import utils.Page;

public class GithubApiTest extends UnitTest {

	@Before
	public void cleanup() {
		Cache.clear();
	}

    @Test
    public void searchLightRepository() throws Exception {
    	Page<LightRepository> page = LightRepository.getPage("homework", 1);
    	assertNotNull(page);
    	assertNotNull(page.data);
    	assertEquals(LightRepository.PAGE_SIZE, page.data.size());

    	LightRepository repo = page.data.get(0);
    	assertEquals("homework", repo.name);
    	assertEquals("homework", repo.owner);
    	assertEquals("Openflow/NoX Homework router implementation", repo.description);
    }

    @Test
    public void getFullRepository() throws Exception {
    	FullRepository repo = FullRepository.get("playframework", "play20");
    	assertNotNull(repo);
    	assertEquals("Play framework 2.0", repo.description);
    	assertEquals("playframework", repo.owner);
    	assertEquals("Play20", repo.name);
    	assertTrue(repo.commits.isEmpty());
    	assertTrue(repo.contributors.isEmpty());
    }

    @Test
    public void getCommits() throws Exception {
    	List<Commit> commits = Commit.findList("playframework", "play20");
    	assertNotNull(commits);
    	assertEquals("fixed typo", commits.get(0).message);
    	assertNotNull(commits.get(0).committedDate);
    }

    @Test
    public void getContributors() throws Exception {
    	List<User> contributors = User.findContributorsList("playframework", "play20");
    	assertNotNull(contributors);
    	assertEquals("pk11", contributors.get(0).login);
    }

    @Test
    public void getRepositoryWithCommitsAndContributors() throws Exception {
    	FullRepository repo = new GetFullRepository("playframework", "play20").now().get();
    	assertNotNull(repo);
    	assertEquals("Play framework 2.0", repo.description);
    	assertEquals("playframework", repo.owner);
    	assertEquals("Play20", repo.name);
    	assertFalse(repo.commits.isEmpty());
    	assertFalse(repo.contributors.isEmpty());
    	assertFalse(repo.contributors.get(0).commits.isEmpty());
    }
}
