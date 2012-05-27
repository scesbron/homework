package models;

import java.util.Date;
import java.util.List;

import play.libs.F.Promise;
import play.libs.WS.HttpResponse;
import utils.Page;
import utils.UrlUtils;

public class Commit extends GithubModel {
	public String author;
	public Date date;
	public String login;
	public String gravatar;
	public int space;
	public int time;

	// STATIC METHODS
	//~~~~~~~~~~~~~~~

	public static final List<Commit> findList(String username, String name, String nethash) {
		return findList(WS(String.format("/%s/%s/network_data_chunk", username, name)), Commit.class, "commits");
	}
}
