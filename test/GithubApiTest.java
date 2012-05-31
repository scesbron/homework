import models.Commit;
import models.Repository;
import models.LightRepository;
import models.User;

import org.junit.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

import jobs.GetCommitList;
import jobs.GetContributorList;
import jobs.GetRepositoryDesc;
import jobs.SearchLightRepositoryPage;

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
    	Page<LightRepository> page = new SearchLightRepositoryPage("homework", 1).now().get();
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
    	Repository repo = new GetRepositoryDesc("playframework", "play20").now().get();
    	assertNotNull(repo);
    	assertEquals("Play framework 2.0", repo.description);
    	assertEquals("playframework", repo.owner);
    	assertEquals("Play20", repo.name);
    	assertTrue(repo.commits.isEmpty());
    	assertTrue(repo.contributors.isEmpty());
    }

    @Test
    public void getCommits() throws Exception {
    	List<Commit> commits = new GetCommitList("playframework", "play20", 1).now().get();
    	assertNotNull(commits);
    	assertEquals("fixed typo", commits.get(0).message);
    	assertNotNull(commits.get(0).committedDate);
    }

    @Test
    public void getContributors() throws Exception {
    	List<User> contributors = new GetContributorList("playframework", "play20").now().get();
    	assertNotNull(contributors);
    	assertEquals("pk11", contributors.get(0).login);
    }
}
