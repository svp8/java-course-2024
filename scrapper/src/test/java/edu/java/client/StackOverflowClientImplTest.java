package edu.java.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.dto.stack.AnswerDto;
import edu.java.dto.stack.BadgeDto;
import edu.java.dto.stack.GeneralResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import wiremock.com.fasterxml.jackson.databind.JsonNode;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;
import wiremock.com.fasterxml.jackson.databind.PropertyNamingStrategy;
import wiremock.com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static edu.java.client.StackOverflowClientImpl.SITE_STACKOVERFLOW;

class StackOverflowClientImplTest {
    public static WireMockServer wireMockServer = new WireMockServer();
    OffsetDateTime offsetDateTime = OffsetDateTime.of(2000, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);

    @BeforeAll
    static void init() {
        wireMockServer.start();
    }

    @AfterAll
    static void end() {
        wireMockServer.stop();
    }

    @Test
    void getAnswersByQuestionId() {
        AnswerDto[] expectedDto =
            new AnswerDto[] {new AnswerDto(1, true, 12, offsetDateTime), new AnswerDto(2, true, 12, offsetDateTime)};
        GeneralResponse<AnswerDto> expected = new GeneralResponse<>();
        expected.setItems(expectedDto);
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .setPropertyNamingStrategy(
                PropertyNamingStrategy.SNAKE_CASE);
        JsonNode node = mapper.valueToTree(expected);
        String testUrl = String.format("/questions/%d/answers?%s", 0, SITE_STACKOVERFLOW);
        stubFor(WireMock.get(urlEqualTo(testUrl))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node)));
        StackOverflowClientImpl stackOverflowClient = new StackOverflowClientImpl(wireMockServer.baseUrl());

        AnswerDto[] actual = stackOverflowClient.getAnswersByQuestionId(0).getItems();

        verify(getRequestedFor(urlEqualTo(testUrl)));
        Assertions.assertArrayEquals(expectedDto, actual);
    }

    @Test
    void getAllBatches() {
        BadgeDto[] expectedDto = new BadgeDto[] {new BadgeDto("123", 1, "213", 21, "test", "test1"),
            new BadgeDto("1231aa", 21, "213ds", 21, "test", "test2")};
        GeneralResponse<BadgeDto> expected = new GeneralResponse<>();
        expected.setItems(expectedDto);
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        JsonNode node = mapper.valueToTree(expected);
        String testUrl = "/badges?"+SITE_STACKOVERFLOW;
        stubFor(WireMock.get(urlEqualTo(testUrl))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node)));
        StackOverflowClientImpl stackOverflowClient = new StackOverflowClientImpl(wireMockServer.baseUrl());

        BadgeDto[] actual = stackOverflowClient.getAllBadges().getItems();

        verify(getRequestedFor(urlEqualTo(testUrl)));
        Assertions.assertArrayEquals(expectedDto, actual);
    }
}
