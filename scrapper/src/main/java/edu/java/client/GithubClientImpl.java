package edu.java.client;

import edu.java.dto.github.BranchDto;
import edu.java.dto.github.RepositoryDto;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

public class GithubClientImpl implements GitHubClient {
    public static final String BASE_URL = "https://api.github.com";
    private final WebClient webClient;

    public GithubClientImpl(String baseUrl) {
        if (baseUrl == null || baseUrl.isBlank()) {
            this.webClient = WebClient.create(BASE_URL);
        } else {
            this.webClient = WebClient.create(baseUrl);
        }
    }

    public GithubClientImpl() {
        this.webClient = WebClient.create(BASE_URL);
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
}
