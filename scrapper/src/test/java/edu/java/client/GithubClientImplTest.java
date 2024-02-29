package edu.java.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import edu.java.dto.github.BranchDto;
import edu.java.dto.github.PullRequestDto;
import edu.java.dto.github.RepositoryDto;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import wiremock.com.fasterxml.jackson.databind.JsonNode;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;
import wiremock.com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

class GithubClientImplTest {

    public static WireMockServer wireMockServer = new WireMockServer();

    @BeforeAll
    static void init() {
        wireMockServer.start();
    }
    @AfterAll
    static void end(){
      wireMockServer.stop();
    }

    @Test
    void fetchRepository() {
        RepositoryDto expected =
            new RepositoryDto(1, "test", OffsetDateTime.of(2000, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC), "testLang", 3);
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        JsonNode node = mapper.valueToTree(expected);
        stubFor(WireMock.get(urlEqualTo("/repos/svp8/java-course-2024"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node)));
        GithubClientImpl githubClient = new GithubClientImpl(wireMockServer.baseUrl());

        RepositoryDto actual = githubClient.fetchRepository("java-course-2024", "svp8");

        verify(getRequestedFor(urlEqualTo("/repos/svp8/java-course-2024")));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void fetchBranchList() {
        List<BranchDto> expected = List.of(new BranchDto("test"), new BranchDto("test2"));
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        JsonNode node = mapper.valueToTree(expected);
        String testUrl = "/repos/svp8/java-course-2024/branches";
        stubFor(WireMock.get(urlEqualTo(testUrl))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node)));
        GithubClientImpl githubClient = new GithubClientImpl(wireMockServer.baseUrl());

        List<BranchDto> actual = githubClient.fetchBranchList("java-course-2024", "svp8");

        verify(getRequestedFor(urlEqualTo(testUrl)));
        Assertions.assertEquals(expected, actual);
    }
    @Test
    void fetchPRList() {
        List<PullRequestDto> expected = List.of(new PullRequestDto(), new PullRequestDto());
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        JsonNode node = mapper.valueToTree(expected);
        String testUrl = "/repos/svp8/java-course-2024/pulls";
        stubFor(WireMock.get(urlEqualTo(testUrl))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node)));
        GithubClientImpl githubClient = new GithubClientImpl(wireMockServer.baseUrl());

        List<PullRequestDto> actual = githubClient.fetchPullRequestList("java-course-2024", "svp8");

        verify(getRequestedFor(urlEqualTo(testUrl)));
        Assertions.assertEquals(expected, actual);
    }
}
