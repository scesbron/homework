package models;

import java.util.ArrayList;
import java.util.List;

public class User {
	public String name;
	public String fullname;
	public String gravatar;

	public List<Commit> commits;

	public User(String name, String fullname, String gravatar) {
		super();
		this.name = name;
		this.fullname = fullname;
		this.gravatar = gravatar;
	}

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
}
