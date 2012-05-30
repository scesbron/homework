package jobs;

import java.util.Collections;
import java.util.Comparator;

import models.Commit;
import models.FullRepository;
import models.User;
import play.jobs.Job;

public class GetFullRepository extends Job<FullRepository> {

	public String owner;
	public String name;

	public GetFullRepository(String owner, String name) {
		this.owner = owner;
		this.name = name;
	}
	@Override
	public FullRepository doJobWithResult() throws Exception {
		FullRepository repo = FullRepository.get(owner, name);
		repo.contributors = User.findContributorsList(owner, name);
    	repo.commits = Commit.findList(owner, name);

    	// Sort users by number of commits
    	Collections.sort(repo.contributors, new Comparator<User>() {
    		public int compare(User user1, User user2) {
    			return user2.commits.size() - user1.commits.size();
    		};
		});
    	return repo;
	}

}
