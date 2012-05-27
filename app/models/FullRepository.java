package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.joda.time.LocalDate;

import play.libs.F.Promise;
import play.libs.WS.HttpResponse;
import utils.UrlUtils;

public class FullRepository extends GithubModel {
	public String username;
	public String name;
	public String nethash;

	public List<User> users;
	public List<User> commiters;

	public List<Commit> commits;

	/**
	 * We initialize commits here because if we do it directly on the commits
	 * field, initialization is not done when the object is created with gson
	 * @return
	 */
	public List<User> getCommiters() {
		if (commiters == null) {
			commiters = new ArrayList<User>();
		}
		return commiters;
	}

	public void setCommits(List<Commit> commits) {
		this.commits = commits;
		for (Commit commit : this.commits) {
			User user = findUserByGravatar(commiters, commit.gravatar);
			if (user != null) {
				user.name = commit.login;
				user.fullname = commit.author;
				user.gravatar = commit.gravatar;
			} else {
				user = new User(commit.login, commit.author, commit.gravatar);
				commiters.add(user);
			}
			user.commits.add(commit);
		}
	}

	private User findUserByGravatar(List<User> list, String name) {
		if (list != null) {
			for (User user : list) {
				if (ObjectUtils.equals(user.gravatar, name)) {
					return user;
				}
			}
		}
		return null;
	}

	public Map<Date, Integer> getCommitRepartitionByDate() {
		Map<Date, Integer> repartition = new HashMap<Date, Integer>();
		if (!CollectionUtils.isEmpty(commits)) {
			LocalDate date = new LocalDate(commits.get(0).date);
			LocalDate endDate = new LocalDate(commits.get(commits.size() -1).date);
			while (date.isBefore(endDate) || date.isEqual(endDate)) {
				repartition.put(date.toDateMidnight().toDate(), getCommitCount(date));
				date = date.plusDays(1);
			}
		}
		return repartition;
	}

	private Integer getCommitCount(LocalDate date) {
		int count = 0;
		for (Commit commit : commits) {
			if (date.equals(new LocalDate(commit.date))) {
				count++;
			}
		}
		return count;
	}

	public Date getMinDate() {
		return CollectionUtils.isEmpty(commits) ? null : commits.get(0).date;
	}

	// STATIC METHODS
	//~~~~~~~~~~~~~~~

	public static final FullRepository get(String username, String name) {
		FullRepository repo = find(WS(String.format("/%s/%s/network_meta", username, name)), FullRepository.class);
		repo.username = username;
		repo.name = name;
		return repo;
	}
}
