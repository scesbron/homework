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
    	Page<LightRepository> page = await(new SearchLightRepositoryPage(query, pageNumber).now());
    	if (page != null && page.data.size() == 1) {
    		LightRepository repo = page.data.get(0);
    		showRepo(repo.username, repo.name);
    	} else {
    		render("@index", page);
    	}
    }

    public static void showRepo(String username, String name) throws Exception {
    	FullRepository repo = await(new GetFullRepository(username, name).now());
    	render(repo, username, name);
    }
}