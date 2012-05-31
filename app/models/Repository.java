package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import play.Logger;
import play.Play;
import play.libs.F.Promise;
import play.libs.WS.HttpResponse;
import utils.UrlUtils;

public class Repository extends LightRepository {

	public DateTime firstCommitDate;
	public DateTime lastCommitDate;
	public String language;
	public String homepage;
	public String url;

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

	public List<User> contributors;
	/**
	 * We initialize commits here because if we do it directly on the commits
	 * field, initialization is not done when the object is created with gson
	 * @return
	 */
	public List<User> getContributors() {
		if (contributors == null) {
			contributors = new ArrayList<User>();
		}
		return contributors;
	}

	public void setCommits(List<Commit> commits) {
		this.commits = commits;
		for (Commit commit : this.commits) {
			User user = findContributor(commit.committer.login);
			if (user == null) {
				Logger.warn("Found a commit for an unknown user : %s", commit.committer);
				user = new User(commit.committer.email, commit.committer.login, commit.committer.name);
				contributors.add(user);
			}
			user.commits.add(commit);
			if (firstCommitDate == null || commit.committedDate.isBefore(firstCommitDate)) {
				firstCommitDate = commit.committedDate;
			}
			if (lastCommitDate == null || commit.committedDate.isAfter(lastCommitDate)) {
				lastCommitDate = commit.committedDate;
			}
		}
	}

	private User findContributor(String login) {
		for (User user : contributors) {
			if (ObjectUtils.equals(user.login, login)) {
				return user;
			}
		}
		return null;
	}

	public List<User> getContributorsWithCommits() {
		List<User> result = new ArrayList<User>();
		for (User user : contributors) {
			if (!user.commits.isEmpty()) {
				result.add(user);
			}
		}
		return result;
	}

	public Map<Date, Integer> getCommitRepartitionByDate() {
		Map<Date, Integer> repartition = new TreeMap<Date, Integer>();
		if (!CollectionUtils.isEmpty(commits)) {
			LocalDate date = new LocalDate(firstCommitDate);
			LocalDate endDate = new LocalDate(lastCommitDate);
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
			if (date.equals(new LocalDate(commit.committedDate))) {
				count++;
			}
		}
		return count;
	}

	// STATIC METHODS
	//~~~~~~~~~~~~~~~

	public static final Repository get(String owner, String name) {
		String url = String.format("/api/v2/json/repos/show/%s/%s", owner, name);
		// Hack for test mode because repos/show/owner/repo cannot be a file
		// It must be a directory when we put contributors file
		if (Play.runingInTestMode()) {
			url += "/detail";
		}
		Repository repo = find(WS(url), Repository.class, "repository");
		return repo;
	}
}
