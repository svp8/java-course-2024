package edu.java.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.dto.stack.AnswerDto;
import edu.java.dto.stack.CommentDto;
import edu.java.dto.stack.GeneralResponse;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import wiremock.com.fasterxml.jackson.databind.JsonNode;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;
import wiremock.com.fasterxml.jackson.databind.PropertyNamingStrategy;
import wiremock.com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static edu.java.client.StackOverflowClientImpl.SITE_STACKOVERFLOW;

class StackOverflowClientImplTest {
    public static WireMockServer wireMockServer=new WireMockServer();
    OffsetDateTime offsetDateTime = OffsetDateTime.of(2000, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);
    static StackOverflowClientImpl stackOverflowClient;

    @BeforeAll
    static void init() {
        wireMockServer.start();
        System.out.println(wireMockServer.baseUrl());
        stackOverflowClient = new StackOverflowClientImpl(wireMockServer.baseUrl(), WebClient.builder());

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
        String testUri = String.format("/questions/%d/answers?%s", 0, SITE_STACKOVERFLOW);
        stubFor(WireMock.get(urlEqualTo(testUri))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node)));

        AnswerDto[] actual = stackOverflowClient.getAnswersByQuestionId(0).getItems();

        verify(getRequestedFor(urlEqualTo(testUri)));
        Assertions.assertArrayEquals(expectedDto, actual);
    }

    @Test
    void getAllComments() {
        CommentDto[] expectedDto = new CommentDto[] {new CommentDto(1, 1, "213", "21", true, offsetDateTime),
            new CommentDto(2, 1, "213", "21", true, offsetDateTime)};
        GeneralResponse<CommentDto> expected = new GeneralResponse<>();
        expected.setItems(expectedDto);
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        JsonNode node = mapper.valueToTree(expected);
        String testUri = String.format("/questions/%d/comments?%s", 0, SITE_STACKOVERFLOW);
        stubFor(WireMock.get(urlEqualTo(testUri))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node)));

        CommentDto[] actual = stackOverflowClient.getCommentsByQuestionId(0).getItems();

        verify(getRequestedFor(urlEqualTo(testUri)));
        Assertions.assertArrayEquals(expectedDto, actual);
    }
}
