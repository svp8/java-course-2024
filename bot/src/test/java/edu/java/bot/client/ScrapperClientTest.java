package edu.java.bot.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.bot.model.Link;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import wiremock.com.fasterxml.jackson.databind.JsonNode;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

class ScrapperClientTest {
    public static WireMockServer wireMockServer = new WireMockServer();
    ScrapperClient scrapperClient = new ScrapperClient(wireMockServer.baseUrl(), WebClient.builder());

    @BeforeAll
    static void init() {
        wireMockServer.start();
    }

    @AfterAll
    static void end() {
        wireMockServer.stop();
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
