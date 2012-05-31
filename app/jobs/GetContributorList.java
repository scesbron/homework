package jobs;

import java.util.List;

import play.jobs.Job;

import models.Commit;
import models.User;

public class GetContributorList extends Job<List<User>> {

	public String owner;
	public String name;

	public GetContributorList(String owner, String name) {
		this.owner = owner;
		this.name = name;
	}
	@Override
	public List<User> doJobWithResult() throws Exception {
		return User.findContributorsList(owner, name);
	}
}
