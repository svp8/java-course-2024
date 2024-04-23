package edu.java.client;

import edu.java.dto.github.BranchDto;
import edu.java.dto.github.PullRequestDto;
import edu.java.dto.github.RepositoryDto;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

public class GithubClientImpl implements GitHubClient {
    public static final String BASE_URL = "https://api.github.com";
    private final WebClient webClient;
    private final String baseUrl;
    private final Retry retry;

    public GithubClientImpl(String baseUrl, WebClient.Builder builder, Retry retry) {
        this.webClient = builder.baseUrl(ClientUtils.getBaseUrl(baseUrl, BASE_URL))
            .build();
        this.retry = retry;
        this.baseUrl = baseUrl;
    }

    public GithubClientImpl(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(BASE_URL).build();
        this.retry = Retry.max(0);
        this.baseUrl = BASE_URL;
    }

    @Override
    public RepositoryDto fetchRepository(String repositoryName, String owner) {
        WebClient.ResponseSpec responseSpec = webClient
            .get()
            .uri(String.format("/repos/%s/%s", owner, repositoryName))
            .retrieve();
        RepositoryDto repositoryDto = responseSpec.bodyToMono(RepositoryDto.class).retryWhen(retry).block();
        return repositoryDto;
    }

    @Override
    public List<BranchDto> fetchBranchList(String repositoryName, String owner) {
        WebClient.ResponseSpec responseSpec = webClient
            .get()
            .uri(String.format("/repos/%s/%s/branches", owner, repositoryName))
            .retrieve();
        List<BranchDto> branches = responseSpec.bodyToMono(new ParameterizedTypeReference<List<BranchDto>>() {
        }).retryWhen(retry).block();
        return branches;
    }

    @Override
    public List<PullRequestDto> fetchPullRequestList(String repositoryName, String owner) {
        WebClient.ResponseSpec responseSpec = webClient
            .get()
            .uri(String.format("/repos/%s/%s/pulls", owner, repositoryName))
            .retrieve();
        List<PullRequestDto> list = responseSpec.bodyToMono(new ParameterizedTypeReference<List<PullRequestDto>>() {
        }).retryWhen(retry).block();
        return list;
    }

}
