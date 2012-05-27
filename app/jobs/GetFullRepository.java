package jobs;

import java.util.Collections;
import java.util.Comparator;

import models.Commit;
import models.FullRepository;
import models.User;
import play.jobs.Job;

public class GetFullRepository extends Job<FullRepository> {

	public String username;
	public String name;

	public GetFullRepository(String username, String name) {
		this.username = username;
		this.name = name;
	}
	@Override
	public FullRepository doJobWithResult() throws Exception {
		FullRepository repo = FullRepository.get(username, name);
    	repo.commits = Commit.findList(username, name, repo.nethash);

    	// Sort users by number of commits
    	Collections.sort(repo.commiters, new Comparator<User>() {
    		public int compare(User user1, User user2) {
    			return user2.commits.size() - user1.commits.size();
    		};
		});
    	return repo;
	}

}
