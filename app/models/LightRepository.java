package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import play.Logger;
import play.cache.Cache;
import play.libs.Crypto;
import play.libs.F.Promise;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;
import utils.Page;
import utils.UrlUtils;

public class LightRepository extends GithubModel {
	public String username;
	public String name;
	public String description;
	public int size;
	@SerializedName(value="private")
	public boolean isPrivate;
	public int watchers;

	public static final int PAGE_SIZE = 100;

	// STATIC METHODS
	//~~~~~~~~~~~~~~~

	public static final Page<LightRepository> getPage(String query, int pageNumber) {
		Page<LightRepository> page = new Page(pageNumber, query, LightRepository.PAGE_SIZE);
		WSRequest request = WS("/api/v2/json/repos/search/" + UrlUtils.encodePathSegment(page.query)).setParameter("start_page", page.number);
		String cacheKey = String.format("#search#%s#%s", query, pageNumber);
		page.data = findCachedList(cacheKey, request, LightRepository.class, "repositories");
		return page;
	}
}
