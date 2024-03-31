package edu.java.bot.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import edu.java.bot.configuration.RetryConfig;
import edu.java.bot.configuration.RetryType;
import edu.java.bot.configuration.RetryUtil;
import edu.java.bot.model.Link;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;
import wiremock.com.fasterxml.jackson.databind.JsonNode;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

class ScrapperClientTest {
    public static WireMockServer wireMockServer = new WireMockServer();
    ScrapperClient scrapperClient = new ScrapperClient(wireMockServer.baseUrl(), WebClient.builder(), Retry.max(100));

    @BeforeAll
    static void init() {
        wireMockServer.start();
    }

    @AfterAll
    static void end() {
        wireMockServer.stop();
    }

    @AfterEach
    void restart() {
        wireMockServer.resetAll();
    }

    @Test
    void retryOk() throws URISyntaxException {
        Link expected = new Link(new URI("http://test.com/test"));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.valueToTree(expected);
        String testUrl = "/track";
        int attempts = 3;
        RetryConfig retryConfig = new RetryConfig(true, RetryType.CONSTANT, attempts, 1, null);
        Retry retry = RetryUtil.getRetry(retryConfig);
        ScrapperClient scrapperClient1 = new ScrapperClient(wireMockServer.baseUrl(), WebClient.builder(), retry);
        for (int i = 0; i < attempts; i++) {
            wireMockServer.stubFor(WireMock.post(WireMock.urlPathTemplate(testUrl))
                .inScenario("test_ok")
                .whenScenarioStateIs(i == 0 ? Scenario.STARTED : String.valueOf(i))
                .willReturn(WireMock.aResponse()
                    .withStatus(403)
                    .withHeader("Content-Type", "application/json")
                    .withBody("response"))
                .willSetStateTo(String.valueOf(i + 1)));
        }
        wireMockServer.stubFor(WireMock.post(urlEqualTo(testUrl))
            .inScenario("test_ok")
            .whenScenarioStateIs(String.valueOf(attempts))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node)));

        Link actual = scrapperClient1.trackLink(0, "http://test.com/test");

        wireMockServer.verify(postRequestedFor(urlEqualTo(testUrl)));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void retryFail() {
        String testUrl = "/track";
        int attempts = 3;
        RetryConfig retryConfig = new RetryConfig(true, RetryType.CONSTANT, attempts, 1, null);
        Retry retry = RetryUtil.getRetry(retryConfig);
        ScrapperClient scrapperClient1 = new ScrapperClient(wireMockServer.baseUrl(), WebClient.builder(), retry);
        for (int i = 0; i < attempts; i++) {
            wireMockServer.stubFor(WireMock.post(WireMock.urlPathTemplate(testUrl))
                .inScenario("test_ok")
                .whenScenarioStateIs(i == 0 ? Scenario.STARTED : String.valueOf(i))
                .willReturn(WireMock.aResponse()
                    .withStatus(403)
                    .withHeader("Content-Type", "application/json")
                    .withBody("response"))
                .willSetStateTo(String.valueOf(i + 1)));
        }
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> scrapperClient1.trackLink(0, "http://test.com/test")
        );
        wireMockServer.verify(exactly(attempts + 1), postRequestedFor(urlEqualTo(testUrl)));

    }

    @Test
    @DisplayName("Should not retry on 403")
    void retryCodes() throws URISyntaxException {
        Link expected = new Link(new URI("http://test.com/test"));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.valueToTree(expected);
        String testUrl = "/track";
        int attempts = 3;
        RetryConfig retryConfig = new RetryConfig(true, RetryType.CONSTANT, attempts, 1, Set.of(404));
        Retry retry = RetryUtil.getRetry(retryConfig);
        ScrapperClient scrapperClient1 = new ScrapperClient(wireMockServer.baseUrl(), WebClient.builder(), retry);
        //403
        wireMockServer.stubFor(WireMock.post(WireMock.urlPathTemplate(testUrl))
            .inScenario("test_ok")
            .whenScenarioStateIs(Scenario.STARTED)
            .willReturn(WireMock.aResponse()
                .withStatus(403)
                .withHeader("Content-Type", "application/json")
                .withBody("response"))
            .willSetStateTo("403"));
        Assertions.assertThrows(
            WebClientResponseException.class,
            () -> scrapperClient1.trackLink(0, "http://test.com/test")
        );
        //404
        wireMockServer.stubFor(WireMock.post(WireMock.urlPathTemplate(testUrl))
            .inScenario("test_ok")
            .whenScenarioStateIs("403")
            .willReturn(WireMock.aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody("response"))
            .willSetStateTo("ok"));
        //ok
        wireMockServer.stubFor(WireMock.post(urlEqualTo(testUrl))
            .inScenario("test_ok")
            .whenScenarioStateIs("ok")
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node)));
        Link actual = scrapperClient1.trackLink(0, "http://test.com/test");
        wireMockServer.verify(exactly(3), postRequestedFor(urlEqualTo(testUrl)));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void trackLink() throws URISyntaxException {
        Link expected = new Link(new URI("http://test.com/test"));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.valueToTree(expected);
        String testUrl = "/track";
        stubFor(WireMock.post(urlEqualTo(testUrl))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node)));

        Link actual = scrapperClient.trackLink(0, "http://test.com/test");

        verify(postRequestedFor(urlEqualTo(testUrl)));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void untrackLink() throws URISyntaxException {

//        ObjectMapper mapper=new ObjectMapper();
//        JsonNode node = mapper.valueToTree(link);
        String testUrl = "/untrack";
        stubFor(WireMock.post(urlEqualTo(testUrl))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
            ));

        scrapperClient.untrackLink(0, "http://test.com/test");

        verify(postRequestedFor(urlEqualTo(testUrl)));
    }

    @Test
    void getLinkList() {
        String testUrl = "/0/all";
        stubFor(WireMock.get(urlEqualTo(testUrl))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
            ));

        scrapperClient.getLinkList(0);

        verify(getRequestedFor(urlEqualTo(testUrl)));
    }

    @Test
    void registerChat() throws URISyntaxException {

        String testUrl = "/register/0";
        stubFor(WireMock.post(urlEqualTo(testUrl))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
            ));

        scrapperClient.registerChat(0);

        verify(postRequestedFor(urlEqualTo(testUrl)));

    }
}
