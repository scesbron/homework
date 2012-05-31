package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;

import play.Logger;
import play.Play;
import play.cache.Cache;
import play.data.binding.types.DateBinder;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;
import utils.DateTimeAdapter;

public class GithubModel implements Serializable {

	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String PUT = "PUT";
	public static final String DELETE = "DELETE";
	private static final String WS_TIMEOUT = Play.configuration.getProperty("ws.timeout", "10s");
	protected static final String DEFAULT_CACHE_DURATION = Play.configuration.getProperty("cache.duration", "30mn");
	private static final String BASE_URL = Play.configuration.getProperty("github.url", "http://github.com");

	/**
	 * Wrap the wsrequest construction by setting a global timeout configurable in application.conf
	 * @param url
	 * @return
	 */
	protected static WSRequest WS(String url) {
		return play.libs.WS.url(BASE_URL + url).timeout(WS_TIMEOUT);
	}

	@SuppressWarnings("unchecked")
	protected static <T> T findCached(String cacheKey, WSRequest request, Class<T> clazz) {
		return findCached(cacheKey, request, clazz, null);
	}
	@SuppressWarnings("unchecked")
	protected static <T> T findCached(String cacheKey, WSRequest request, Class<T> clazz, String name) {
		T result = (T) Cache.get(cacheKey);
		if (result == null) {
			result = find(request, clazz, name);
			Cache.set(cacheKey, result, DEFAULT_CACHE_DURATION);
		}
		return result;
	}

	protected static <T> T find(WSRequest request, Class<T> clazz) {
		return find(request, clazz, null);
	}
	protected static <T> T find(WSRequest request, Class<T> clazz, String name) {
		HttpResponse response = logRequest(request).get();
		return find(response, clazz, name);
	}

	protected static final <T> T find(HttpResponse response, Class<T> clazz) {
		return find(response, clazz, null);
	}
	protected static final <T> T find(HttpResponse response, Class<T> clazz, String name) {
		T result = null;
		if (response.success()) {
			Gson gson = createGson();
			JsonObject jsonResponse = response.getJson().getAsJsonObject();
			if (name != null) {
				jsonResponse = jsonResponse.get(name).getAsJsonObject();
			}
			if (Logger.isDebugEnabled()) {
				Logger.debug(gson.toJson(result));
			}
			result = gson.fromJson(jsonResponse, clazz);
		} else {
			Logger.error("Error while getting instance of %s, response code is %s", clazz.getSimpleName(), response.getStatus());
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	protected static <T> List<T> findCachedList(String cacheKey, WSRequest request, Class<T> clazz, String arrayName) {
		List<T> liste = (List<T>) Cache.get(cacheKey);
		if (liste == null || Play.runingInTestMode()) {
			liste = findList(request, clazz, arrayName);
			Cache.set(cacheKey, liste, DEFAULT_CACHE_DURATION);
		}
		return liste;
	}

	protected static <T> List<T> findList(WSRequest request, Class<T> clazz, String arrayName) {
		HttpResponse response = logRequest(request).get();
		return findList(response, clazz, arrayName);
	}

	protected static final <T> List<T> findList(HttpResponse response, Class<T> clazz, String arrayName) {
		List<T> list = new ArrayList<T>();
		if (response.success()) {
			Gson gson = createGson();
			JsonObject jsonResponse = response.getJson().getAsJsonObject();
			if (Logger.isDebugEnabled()) {
				Logger.debug(gson.toJson(jsonResponse));
			}
			JsonArray array = jsonResponse.get(arrayName).getAsJsonArray();
			for (JsonElement elem : array) {
				list.add(gson.fromJson(elem, clazz));
			}
		} else {
			Logger.error("Error while getting list of %s, response code is %s", clazz.getSimpleName(), response.getStatus());
		}
		return list;
	}

	protected static WSRequest logRequest(WSRequest request) {
		if (Logger.isDebugEnabled()) {
			Logger.debug("Appel du service %s", request.url);
			for (Entry<String, Object> entry : request.parameters.entrySet()) {
				Logger.debug("%s=%s", entry.getKey(), entry.getValue());
			}
		}
		return request;
	}

	public static Gson createGson() {
		GsonBuilder builder = new GsonBuilder();
		// TODO better date handling
		builder.registerTypeAdapter(DateTime.class, new DateTimeAdapter());
		if (Logger.isDebugEnabled()) {
			builder.setPrettyPrinting();
		}
		return builder.create();
	}
}
