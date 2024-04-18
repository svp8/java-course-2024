package edu.java.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import edu.java.configuration.RetryConfig;
import edu.java.configuration.RetryType;
import edu.java.configuration.RetryUtil;
import edu.java.dto.stack.AnswerDto;
import edu.java.dto.stack.CommentDto;
import edu.java.dto.stack.GeneralResponse;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
import wiremock.com.fasterxml.jackson.databind.PropertyNamingStrategy;
import wiremock.com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static edu.java.client.StackOverflowClientImpl.SITE_STACKOVERFLOW;

class StackOverflowClientImplTest {
    public static WireMockServer wireMockServer = new WireMockServer();
    OffsetDateTime offsetDateTime = OffsetDateTime.of(2000, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);
    static StackOverflowClientImpl stackOverflowClient;

    @BeforeAll
    static void init() {
        wireMockServer.start();
        RetryConfig retryConfig = new RetryConfig(true, RetryType.LINEAR, 100, 1, null);
        Retry retry = RetryUtil.getRetry(retryConfig);
        stackOverflowClient =
            new StackOverflowClientImpl(wireMockServer.baseUrl(), WebClient.builder(), retry);
    }

    @AfterAll
    static void end() {
        wireMockServer.stop();
    }

    @AfterEach
    public void restart() {
        wireMockServer.resetAll();
    }

    @Test
    @Transactional
    @Rollback
    void retryOk() {
        AnswerDto[] expectedDto =
            new AnswerDto[] {new AnswerDto(1, true, offsetDateTime, 12, offsetDateTime),
                new AnswerDto(2, true, offsetDateTime, 12, offsetDateTime)};
        GeneralResponse<AnswerDto> expected = new GeneralResponse<>();
        expected.setItems(expectedDto);
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .setPropertyNamingStrategy(
                PropertyNamingStrategy.SNAKE_CASE);
        JsonNode node = mapper.valueToTree(expected);
        String testUri = String.format("/questions/%d/answers?%s", 0, SITE_STACKOVERFLOW);
        int attempts = 3;
        RetryConfig retryConfig = new RetryConfig(true, RetryType.LINEAR, attempts, 1, null);
        Retry retry = RetryUtil.getRetry(retryConfig);
        StackOverflowClientImpl stackOverflowClient2 =
            new StackOverflowClientImpl(wireMockServer.baseUrl(), WebClient.builder(), retry);
        for (int i = 0; i < attempts; i++) {
            wireMockServer.stubFor(WireMock.get(urlEqualTo(testUri))
                .inScenario("test_ok")
                .whenScenarioStateIs(i == 0 ? Scenario.STARTED : String.valueOf(i))
                .willReturn(WireMock.aResponse()
                    .withStatus(403)
                    .withHeader("Content-Type", "application/json")
                    .withBody("response"))
                .willSetStateTo(String.valueOf(i + 1)));
        }

        wireMockServer.stubFor(WireMock.get(urlEqualTo(testUri))
            .inScenario("test_ok")
            .whenScenarioStateIs(String.valueOf(attempts))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node)));

        AnswerDto[] actual = stackOverflowClient2.getAnswersByQuestionId(0).getItems();

        wireMockServer.verify(exactly(attempts + 1), getRequestedFor(urlEqualTo(testUri)));
        Assertions.assertArrayEquals(expectedDto, actual);
    }

    @Test
    @Transactional
    @Rollback
    void retryFail() {
        AnswerDto[] expectedDto =
            new AnswerDto[] {new AnswerDto(1, true, offsetDateTime, 12, offsetDateTime),
                new AnswerDto(2, true, offsetDateTime, 12, offsetDateTime)};
        GeneralResponse<AnswerDto> expected = new GeneralResponse<>();
        expected.setItems(expectedDto);
        String testUri = String.format("/questions/%d/answers?%s", 0, SITE_STACKOVERFLOW);
        int attempts = 3;
        RetryConfig retryConfig = new RetryConfig(true, RetryType.LINEAR, attempts, 1, null);
        Retry retry = RetryUtil.getRetry(retryConfig);
        StackOverflowClientImpl stackOverflowClient2 =
            new StackOverflowClientImpl(wireMockServer.baseUrl(), WebClient.builder(), retry);
        for (int i = 0; i < attempts; i++) {

            wireMockServer.stubFor(WireMock.get(urlEqualTo(testUri))
                .inScenario("test_fail")
                .whenScenarioStateIs(i == 0 ? Scenario.STARTED : String.valueOf(i))
                .willReturn(WireMock.aResponse()
                    .withStatus(403)
                    .withHeader("Content-Type", "application/json")
                    .withBody("response"))
                .willSetStateTo(String.valueOf(i + 1)));
        }

        Assertions.assertThrows(IllegalStateException.class, () -> stackOverflowClient2.getAnswersByQuestionId(0));

        wireMockServer.verify(exactly(attempts + 1), getRequestedFor(urlEqualTo(testUri)));
    }

    @Test
    @DisplayName("Should retry on 404")
    void retryCodes() throws URISyntaxException {
        AnswerDto[] expectedDto =
            new AnswerDto[] {new AnswerDto(1, true, offsetDateTime, 12, offsetDateTime),
                new AnswerDto(2, true, offsetDateTime, 12, offsetDateTime)};
        GeneralResponse<AnswerDto> expected = new GeneralResponse<>();
        expected.setItems(expectedDto);
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .setPropertyNamingStrategy(
                PropertyNamingStrategy.SNAKE_CASE);
        JsonNode node = mapper.valueToTree(expected);
        String testUri = String.format("/questions/%d/answers?%s", 0, SITE_STACKOVERFLOW);
        int attempts = 3;
        RetryConfig retryConfig = new RetryConfig(true, RetryType.LINEAR, attempts, 1, Set.of(404));
        Retry retry = RetryUtil.getRetry(retryConfig);
        StackOverflowClientImpl stackOverflowClient2 =
            new StackOverflowClientImpl(wireMockServer.baseUrl(), WebClient.builder(), retry);
        //403
        wireMockServer.stubFor(WireMock.get(urlEqualTo(testUri))
            .inScenario("test_ok")
            .whenScenarioStateIs(Scenario.STARTED)
            .willReturn(WireMock.aResponse()
                .withStatus(403)
                .withHeader("Content-Type", "application/json")
                .withBody("response"))
            .willSetStateTo("403"));
        Assertions.assertThrows(
            WebClientResponseException.class,
            () -> stackOverflowClient2.getAnswersByQuestionId(0)
        );
        //404
        wireMockServer.stubFor(WireMock.get(urlEqualTo(testUri))
            .inScenario("test_ok")
            .whenScenarioStateIs("403")
            .willReturn(WireMock.aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody("response"))
            .willSetStateTo("ok"));
        //ok
        wireMockServer.stubFor(WireMock.get(urlEqualTo(testUri))
            .inScenario("test_ok")
            .whenScenarioStateIs("ok")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node)));
        AnswerDto[] actual = stackOverflowClient.getAnswersByQuestionId(0).getItems();
        wireMockServer.verify(exactly(3), getRequestedFor(urlEqualTo(testUri)));
        Assertions.assertArrayEquals(expectedDto, actual);
    }

    @Test
    @Transactional
    @Rollback
    void getAnswersByQuestionId() {
        AnswerDto[] expectedDto =
            new AnswerDto[] {new AnswerDto(1, true, offsetDateTime, 12, offsetDateTime),
                new AnswerDto(2, true, offsetDateTime, 12, offsetDateTime)};
        GeneralResponse<AnswerDto> expected = new GeneralResponse<>();
        expected.setItems(expectedDto);
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .setPropertyNamingStrategy(
                PropertyNamingStrategy.SNAKE_CASE);
        JsonNode node = mapper.valueToTree(expected);
        String testUri = String.format("/questions/%d/answers?%s", 0, SITE_STACKOVERFLOW);
        wireMockServer.stubFor(WireMock.get(urlEqualTo(testUri))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node)));

        AnswerDto[] actual = stackOverflowClient.getAnswersByQuestionId(0).getItems();

        wireMockServer.verify(getRequestedFor(urlEqualTo(testUri)));
        Assertions.assertArrayEquals(expectedDto, actual);
    }

    @Test
    @Transactional
    @Rollback
    void getAllComments() {
        CommentDto[] expectedDto = new CommentDto[] {new CommentDto(1, 1, "213", "21", true, offsetDateTime),
            new CommentDto(2, 1, "213", "21", true, offsetDateTime)};
        GeneralResponse<CommentDto> expected = new GeneralResponse<>();
        expected.setItems(expectedDto);
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        JsonNode node = mapper.valueToTree(expected);
        String testUri = String.format("/questions/%d/comments?%s", 0, SITE_STACKOVERFLOW);
        wireMockServer.stubFor(WireMock.get(urlEqualTo(testUri))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node)));

        CommentDto[] actual = stackOverflowClient.getCommentsByQuestionId(0).getItems();

        wireMockServer.verify(getRequestedFor(urlEqualTo(testUri)));
        Assertions.assertArrayEquals(expectedDto, actual);
    }
}
