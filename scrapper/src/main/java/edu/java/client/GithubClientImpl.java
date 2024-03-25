package edu.java.client;

import edu.java.dto.github.BranchDto;
import edu.java.dto.github.PullRequestDto;
import edu.java.dto.github.RepositoryDto;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

public class GithubClientImpl implements GitHubClient {
    public static final String BASE_URL = "https://api.github.com";
    private final WebClient webClient;

    public GithubClientImpl(String baseUrl, WebClient.Builder builder) {
        this.webClient = builder.baseUrl(ClientUtils.getBaseUrl(baseUrl, BASE_URL)).build();
    }

    public GithubClientImpl(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(BASE_URL).build();
    }

    @Override
    public RepositoryDto fetchRepository(String repositoryName, String owner) {
        WebClient.ResponseSpec responseSpec = webClient
            .get()
            .uri(String.format("/repos/%s/%s", owner, repositoryName))
            .retrieve();
        RepositoryDto repositoryDto = responseSpec.bodyToMono(RepositoryDto.class).block();
        return repositoryDto;
    }

    @Override
    public List<BranchDto> fetchBranchList(String repositoryName, String owner) {
        WebClient.ResponseSpec responseSpec = webClient
            .get()
            .uri(String.format("/repos/%s/%s/branches", owner, repositoryName))
            .retrieve();
        List<BranchDto> branches = responseSpec.bodyToMono(new ParameterizedTypeReference<List<BranchDto>>() {
        }).block();
        return branches;
    }

    @Override
    public List<PullRequestDto> fetchPullRequestList(String repositoryName, String owner) {
        WebClient.ResponseSpec responseSpec = webClient
            .get()
            .uri(String.format("/repos/%s/%s/pulls", owner, repositoryName))
            .retrieve();
        List<PullRequestDto> list = responseSpec.bodyToMono(new ParameterizedTypeReference<List<PullRequestDto>>() {
        }).block();
        return list;
    }
}
