package controllers;

import play.mvc.Controller;
import play.mvc.results.RenderJson;
import jobs.GetFullRepository;
import jobs.SearchLightRepositoryPage;
import models.GithubModel;
import models.LightRepository;
import models.Repository;
import utils.Page;

/**
 * Controller qui gère les appels de service pour l'api json
 * @author sebastien
 *
 */
public class JsonService extends Controller {

	public static void search(String query, int page) {
    	Page<LightRepository> result = await(new SearchLightRepositoryPage(query, page).now());
    	renderJSON(result);
	}

	public static void show(String owner, String name) {
		Repository repo = await(new GetFullRepository(owner, name).now());
		renderJSON(GithubModel.createGson().toJson(repo));
	}
}
