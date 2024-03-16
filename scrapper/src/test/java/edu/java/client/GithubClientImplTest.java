package edu.java.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.dto.github.BranchDto;
import edu.java.dto.github.PullRequestDto;
import edu.java.dto.github.RepositoryDto;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import wiremock.com.fasterxml.jackson.databind.JsonNode;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;
import wiremock.com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

class GithubClientImplTest {

    public static WireMockServer wireMockServer ;
    private GithubClientImpl githubClient = new GithubClientImpl(wireMockServer.baseUrl(), WebClient.builder());

    @BeforeAll
    static void init() {
        wireMockServer= new WireMockServer( 8908);
        wireMockServer.start();

    }

    @AfterAll
    static void end() {
        wireMockServer.stop();
    }

    @Test
    @Rollback
    @Transactional
    void fetchRepository() {
        RepositoryDto expected =
            new RepositoryDto(1, "test", OffsetDateTime.of(2000, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC), "testLang", 3);
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        JsonNode node = mapper.valueToTree(expected);
        wireMockServer.stubFor(WireMock.get(urlEqualTo("/repos/svp8/java-course-2024"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node)));

        RepositoryDto actual = githubClient.fetchRepository("java-course-2024", "svp8");

        wireMockServer.verify(getRequestedFor(urlEqualTo("/repos/svp8/java-course-2024")));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Rollback
    @Transactional
    void fetchBranchList() {
        List<BranchDto> expected = List.of(new BranchDto("test"), new BranchDto("test2"));
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        JsonNode node = mapper.valueToTree(expected);
        String testUrl = "/repos/svp8/java-course-2024/branches";
        wireMockServer.stubFor(WireMock.get(urlEqualTo(testUrl))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node)));
        List<BranchDto> actual = githubClient.fetchBranchList("java-course-2024", "svp8");

        wireMockServer.verify(getRequestedFor(urlEqualTo(testUrl)));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Rollback
    @Transactional
    void fetchPRList() {
        List<PullRequestDto> expected = List.of(new PullRequestDto(), new PullRequestDto());
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        JsonNode node = mapper.valueToTree(expected);
        String testUrl = "/repos/svp8/java-course-2024/pulls";
       wireMockServer. stubFor(WireMock.get(urlEqualTo(testUrl))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node)));


        List<PullRequestDto> actual = githubClient.fetchPullRequestList("java-course-2024", "svp8");

        wireMockServer.verify(getRequestedFor(urlEqualTo(testUrl)));
        Assertions.assertEquals(expected, actual);
    }
}
