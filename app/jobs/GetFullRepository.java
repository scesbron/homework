package jobs;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import play.jobs.Job;
import play.libs.F;
import play.libs.F.T5;
import models.Commit;
import models.Repository;
import models.User;

public class GetFullRepository extends Job<Repository> {

	public String owner;
	public String name;

	public GetFullRepository(String owner, String name) {
		this.owner = owner;
		this.name = name;
	}
	@Override
	public Repository doJobWithResult() throws Exception {
    	T5<Repository, List<User>, List<Commit>, List<Commit>, List<Commit>> responses = F.Promise.wait5(
    			new GetRepositoryDesc(owner, name).now(),
    			new GetContributorList(owner, name).now(),
    			new GetCommitList(owner, name, 1).now(),
    			new GetCommitList(owner, name, 2).now(),
    			new GetCommitList(owner, name, 3).now()).get();

    	Repository repo = responses._1;
    	repo.contributors = responses._2;
    	List<Commit> commits = responses._3;
    	commits.addAll(responses._4);
    	commits.addAll(responses._5);
    	repo.commits = commits;
    	// Sort users by number of commits
    	Collections.sort(repo.contributors, new Comparator<User>() {
    		public int compare(User user1, User user2) {
    			return user2.commits.size() - user1.commits.size();
    		};
		});
    	return repo;
	}
}
