package jobs;

import play.jobs.Job;
import models.Repository;

/**
 * Job that return basic destails for a repository
 * @author sebastien
 *
 */
public class GetRepositoryDesc extends Job<Repository> {

	public String owner;
	public String name;

	public GetRepositoryDesc(String owner, String name) {
		this.owner = owner;
		this.name = name;
	}
	@Override
	public Repository doJobWithResult() throws Exception {
		return Repository.get(owner, name);
	}
}
