package edu.java.bot.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.bot.model.Link;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import wiremock.com.fasterxml.jackson.databind.JsonNode;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.*;

class ScrapperClientTest {
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
    void trackLink() throws URISyntaxException {
         Link expected =new Link(new URI("http://test.com/test"));
        ObjectMapper mapper=new ObjectMapper();
        JsonNode node = mapper.valueToTree(expected);
        String testUrl = "/track";
        stubFor(WireMock.post(urlEqualTo(testUrl))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node)));
        ScrapperClient scrapperClient = new ScrapperClient(wireMockServer.baseUrl());

        Link actual = scrapperClient.trackLink(0, "http://test.com/test");

        verify(postRequestedFor(urlEqualTo(testUrl)));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void untrackLink() {
    }

    @Test
    void getLinkList() {
    }

    @Test
    void registerChat() throws URISyntaxException {


        String testUrl = "/register/0";
        stubFor(WireMock.post(urlEqualTo(testUrl))
            .willReturn(aResponse()
                .withStatus(403)
                .
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                ));
        ScrapperClient scrapperClient = new ScrapperClient(wireMockServer.baseUrl());

        boolean actual = scrapperClient.registerChat(0);

        verify(postRequestedFor(urlEqualTo(testUrl)));
        Assertions.assertEquals(false, actual);
    }
}
