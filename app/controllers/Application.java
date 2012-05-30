package controllers;

import play.*;
import play.libs.F.Promise;
import play.libs.WS.HttpResponse;
import play.mvc.*;
import utils.Page;

import java.util.*;

import jobs.GetFullRepository;
import jobs.SearchLightRepositoryPage;

import models.Commit;
import models.FullRepository;
import models.LightRepository;

public class Application extends Controller {

    public static void index() {
        render();
    }

    public static void searchRepo(int pageNumber, String query) throws Exception {
    	// No async because it breaks tests
    	Page<LightRepository> page = new SearchLightRepositoryPage(query, pageNumber).now().get();
    	if (page != null && page.data.size() == 1) {
    		LightRepository repo = page.data.get(0);
    		showRepo(repo.owner, repo.name);
    	} else {
    		render("@index", page);
    	}
    }

    public static void showRepo(String username, String name) throws Exception {
    	// No async because it breaks tests
    	FullRepository repo = new GetFullRepository(username, name).now().get();
    	render(repo, username, name);
    }
}