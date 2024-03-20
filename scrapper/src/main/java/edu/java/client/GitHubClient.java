package edu.java.client;

import edu.java.dto.github.BranchDto;
import edu.java.dto.github.PullRequestDto;
import edu.java.dto.github.RepositoryDto;
import java.util.List;

public interface GitHubClient  {
    RepositoryDto fetchRepository(String repositoryName, String owner);

    List<BranchDto> fetchBranchList(String repositoryName, String owner);

    List<PullRequestDto> fetchPullRequestList(String repositoryName, String owner);

}
