package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import com.google.gson.annotations.SerializedName;

import play.libs.F.Promise;
import play.libs.WS.HttpResponse;
import utils.Page;
import utils.UrlUtils;

public class Commit extends GithubModel {

	public String id;
	public String message;

	public User author;
	public User committer;

	@SerializedName("authored_date")
	public DateTime authoredDate;
	@SerializedName("committed_date")
	public DateTime committedDate;

	public Commit(User user, DateTime date) {
		this.author = user;
		this.committer = user;
		this.authoredDate = date;
		this.committedDate = date;
	}

	// STATIC METHODS
	//~~~~~~~~~~~~~~~

	public static final List<Commit> findList(String owner, String name, int page) {
		return findList(owner, name, "master", page);
	}

	public static final List<Commit> findList(String owner, String name, String branch, int page) {
		if (page < 1) {
			page = 1;
		}
		String url = String.format("/api/v2/json/commits/list/%s/%s/%s", owner, name, branch);
		return new ArrayList<Commit>(findCachedList(url + "/" + page, WS(url).setParameter("page", page), Commit.class, "commits"));
	}
}
