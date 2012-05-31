package jobs;

import java.util.List;

import models.Commit;
import models.Repository;

import play.jobs.Job;
import play.libs.F;

/**
 * Job that returns a page of commits for a repository
 * @author sebastien
 *
 */
public class GetCommitList extends Job<List<Commit>>{

	public String owner;
	public String name;
	public int page;

	public GetCommitList(String owner, String name, int page) {
		this.owner = owner;
		this.name = name;
		this.page = page;
	}
	@Override
	public List<Commit> doJobWithResult() throws Exception {
		return Commit.findList(owner, name, page);
	}
}
