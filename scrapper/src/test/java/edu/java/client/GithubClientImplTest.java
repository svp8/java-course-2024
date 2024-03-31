package edu.java.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import edu.java.configuration.RetryConfig;
import edu.java.configuration.RetryType;
import edu.java.configuration.RetryUtil;
import edu.java.dto.github.BranchDto;
import edu.java.dto.github.PullRequestDto;
import edu.java.dto.github.RepositoryDto;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;
import wiremock.com.fasterxml.jackson.databind.JsonNode;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;
import wiremock.com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

class GithubClientImplTest {

    public static WireMockServer wireMockServer;
    private static GithubClientImpl githubClient;

    @BeforeAll
    static void init() {
        wireMockServer = new WireMockServer(8908);
        wireMockServer.start();
        RetryConfig retryConfig = new RetryConfig(true, RetryType.LINEAR, 100, 1, null);
        Retry retry = RetryUtil.getRetry(retryConfig);
        githubClient = new GithubClientImpl(wireMockServer.baseUrl(), WebClient.builder(), retry);
    }

    @AfterEach
    public void restart() {
        wireMockServer.resetAll();
    }

    @AfterAll
    static void end() {
        wireMockServer.stop();
    }

    @Test
    @Rollback
    @Transactional
    void retryConstantTest() {
        int attempts = 3;
        RetryConfig retryConfig = new RetryConfig(true, RetryType.LINEAR, attempts, 1, null);
        Retry retry = RetryUtil.getRetry(retryConfig);
        GithubClientImpl githubClient2 = new GithubClientImpl(wireMockServer.baseUrl(), WebClient.builder(), retry);
        RepositoryDto expected =
            new RepositoryDto(1, "test", OffsetDateTime.of(2000, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC), "testLang", 3);
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        JsonNode node = mapper.valueToTree(expected);
        for (int i = 0; i < attempts; i++) {

            wireMockServer.stubFor(WireMock.get(WireMock.urlPathTemplate("/repos/svp8/java-course-2024"))
                .inScenario("test_ok")
                .whenScenarioStateIs(i == 0 ? Scenario.STARTED : String.valueOf(i))
                .willReturn(WireMock.aResponse()
                    .withStatus(403)
                    .withHeader("Content-Type", "application/json")
                    .withBody("response"))
                .willSetStateTo(String.valueOf(i + 1)));

        }
        wireMockServer.stubFor(WireMock.get(urlEqualTo("/repos/svp8/java-course-2024"))
            .inScenario("test_ok")
            .whenScenarioStateIs(String.valueOf(attempts))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node)));

        RepositoryDto actual = githubClient2.fetchRepository("java-course-2024", "svp8");

        wireMockServer.verify(exactly(attempts + 1), getRequestedFor(urlEqualTo("/repos/svp8/java-course-2024")));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Rollback
    @Transactional
    void retryFailTest() {
        int attempts = 2;
        RetryConfig retryConfig = new RetryConfig(true, RetryType.LINEAR, attempts, 1, null);
        Retry retry = RetryUtil.getRetry(retryConfig);
        GithubClientImpl githubClient2 = new GithubClientImpl(wireMockServer.baseUrl(), WebClient.builder(), retry);
        for (int i = 0; i < attempts; i++) {
            wireMockServer.stubFor(WireMock.get(WireMock.urlPathTemplate("/repos/svp8/java-course-2024"))
                .inScenario("test_fail")
                .whenScenarioStateIs(i == 0 ? Scenario.STARTED : String.valueOf(i))
                .willReturn(WireMock.aResponse()
                    .withStatus(403)
                    .withHeader("Content-Type", "application/json")
                    .withBody("response"))
                .willSetStateTo(String.valueOf(i + 1)));

        }

        Assertions.assertThrows(IllegalStateException.class,
            () -> githubClient2.fetchRepository("java-course-2024", "svp8"));

    }

    @Test
    @DisplayName("Should retry on 404")
    void retryCodes() throws URISyntaxException {
        int attempts = 3;
        RetryConfig retryConfig = new RetryConfig(true, RetryType.LINEAR, attempts, 1, Set.of(404));
        Retry retry = RetryUtil.getRetry(retryConfig);
        GithubClientImpl githubClient2 = new GithubClientImpl(wireMockServer.baseUrl(), WebClient.builder(), retry);
        RepositoryDto expected =
            new RepositoryDto(1, "test", OffsetDateTime.of(2000, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC), "testLang", 3);
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        JsonNode node = mapper.valueToTree(expected);
        String testUri = "/repos/svp8/java-course-2024";
        //403
        wireMockServer.stubFor(WireMock.get(urlEqualTo(testUri))
            .inScenario("test_404")
            .whenScenarioStateIs(Scenario.STARTED)
            .willReturn(WireMock.aResponse()
                .withStatus(403)
                .withHeader("Content-Type", "application/json")
                .withBody("response"))
            .willSetStateTo("403"));
        Assertions.assertThrows(
            WebClientResponseException.class,
            () -> githubClient2.fetchRepository("java-course-2024", "svp8")
        );
        //404
        wireMockServer.stubFor(WireMock.get(urlEqualTo(testUri))
            .inScenario("test_404")
            .whenScenarioStateIs("403")
            .willReturn(WireMock.aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody("response"))
            .willSetStateTo("ok"));
        //ok
        wireMockServer.stubFor(WireMock.get(urlEqualTo(testUri))
            .inScenario("test_404")
            .whenScenarioStateIs("ok")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node)));
        RepositoryDto actual = githubClient2.fetchRepository("java-course-2024", "svp8");
        wireMockServer.verify(exactly(3), getRequestedFor(urlEqualTo(testUri)));
        Assertions.assertEquals(expected, actual);
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
        wireMockServer.stubFor(WireMock.get(urlEqualTo(testUrl))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node)));

        List<PullRequestDto> actual = githubClient.fetchPullRequestList("java-course-2024", "svp8");

        wireMockServer.verify(getRequestedFor(urlEqualTo(testUrl)));
        Assertions.assertEquals(expected, actual);
    }
}
