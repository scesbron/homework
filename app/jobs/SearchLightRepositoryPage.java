package jobs;

import models.LightRepository;
import play.jobs.Job;
import utils.Page;

/**
 * Jobs that search for reposiories that match the given query
 * @author sebastien
 *
 */
public class SearchLightRepositoryPage extends Job<Page<LightRepository>> {

	public String query;
	public int pageNumber;

	public SearchLightRepositoryPage(String query, int pageNumber) {
		this.query = query;
		this.pageNumber = pageNumber;
	}

	@Override
	public Page<LightRepository> doJobWithResult() throws Exception {
		return LightRepository.getPage(query, pageNumber);
	}
}
