package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class User extends GithubModel {
	public String email;
	public String name;
	public String login;
	@SerializedName("gravatar_id")
	public String gravatarId;
	public String company;
	public int contributions;
	public String location;

	public List<Commit> commits;

	/**
	 * We initialize commits here because if we do it directly on the commits
	 * field, initialization is not done when the object is created with gson
	 * @return
	 */
	public List<Commit> getCommits() {
		if (commits == null) {
			commits = new ArrayList<Commit>();
		}
		return commits;
	}

	public User(String email, String login, String name) {
		this.email = email;
		this.login = login;
		this.name = name;
	}

	public String getName() {
		return name == null ? login : name;
	}

	public String toString() {
		return String.format("User[%s]", email);
	}

	// STATIC METHODS
	//~~~~~~~~~~~~~~~

	public static final List<User> findContributorsList(String owner, String name) {
		String url = String.format("/api/v2/json/repos/show/%s/%s/contributors", owner, name);
		return new ArrayList<User>(findCachedList(url, WS(url), User.class, "contributors"));
	}
}
