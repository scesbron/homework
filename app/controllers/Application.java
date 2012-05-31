package controllers;

import play.*;
import play.libs.F;
import play.libs.F.Promise;
import play.libs.F.T3;
import play.libs.F.T5;
import play.libs.WS.HttpResponse;
import play.mvc.*;
import utils.Page;

import java.util.*;

import jobs.GetCommitList;
import jobs.GetContributorList;
import jobs.GetFullRepository;
import jobs.GetRepositoryDesc;
import jobs.SearchLightRepositoryPage;

import models.Commit;
import models.Repository;
import models.LightRepository;
import models.User;

/**
 * Controller that handle html pages
 * @author sebastien
 *
 */
public class Application extends Controller {

    public static void index() {
        render();
    }

    public static void searchRepo(int pageNumber, String query) throws Exception {
    	Page<LightRepository> page = await(new SearchLightRepositoryPage(query, pageNumber).now());
    	if (page != null && page.data.size() == 1) {
    		LightRepository repo = page.data.get(0);
    		showRepo(repo.owner, repo.name);
    	} else {
    		render("@index", page);
    	}
    }

    public static void showRepo(String username, String name) throws Exception {
    	Repository repo = await(new GetFullRepository(username, name).now());
    	render(repo, username, name);
    }
}