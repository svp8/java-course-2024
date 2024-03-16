package edu.java.updater;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.client.BotClient;
import edu.java.client.StackOverflowClient;
import edu.java.client.StackOverflowClientImpl;
import edu.java.dto.stack.AnswerDto;
import edu.java.dto.stack.CommentDto;
import edu.java.dto.stack.GeneralResponse;
import edu.java.entity.LinkEntity;
import edu.java.repository.ChatLinkRepository;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import edu.java.repository.stack.AnswerRepository;
import edu.java.repository.stack.CommentRepository;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import wiremock.com.fasterxml.jackson.databind.JsonNode;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;
import wiremock.com.fasterxml.jackson.databind.PropertyNamingStrategy;
import wiremock.com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static edu.java.client.StackOverflowClientImpl.SITE_STACKOVERFLOW;

@SpringBootTest
class StackUpdaterTest extends IntegrationTest {

    private StackOverflowClient stackOverflowClient =
        new StackOverflowClientImpl(wireMockServer.baseUrl(), WebClient.builder());
    @AfterAll
    static void end() {
        wireMockServer.stop();
    }
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private ChatLinkRepository chatLinkRepository;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private ChatRepository chatRepository;

    public static WireMockServer wireMockServer = new WireMockServer(9081);
    private BotClient botClient = new BotClient(wireMockServer.baseUrl(), WebClient.builder());
    static String testUriComment = String.format("/questions/57626072/comments?%s", SITE_STACKOVERFLOW);
    static String testUriAnswer = String.format("/questions/57626072/answers?%s", SITE_STACKOVERFLOW);
    static String testUrlClient = "/send";
    static OffsetDateTime offsetDateTime = OffsetDateTime.of(2000, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);

    @BeforeAll
    static void init() {
        wireMockServer.start();

        AnswerDto[] expectedDto =
            new AnswerDto[] {new AnswerDto(1, true, offsetDateTime,12, offsetDateTime),
                new AnswerDto(2, true, offsetDateTime,12, offsetDateTime)};
        GeneralResponse<AnswerDto> expected = new GeneralResponse<>();
        expected.setItems(expectedDto);
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .setPropertyNamingStrategy(
                PropertyNamingStrategy.SNAKE_CASE);
        JsonNode node = mapper.valueToTree(expected);

        wireMockServer.stubFor(WireMock.get(urlEqualTo(testUriAnswer))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node)));

        CommentDto[] commentDtos = new CommentDto[] {
            new CommentDto(1, 1, "213", "21", true, offsetDateTime),
            new CommentDto(2, 1, "213", "21", true, offsetDateTime)};
        GeneralResponse<CommentDto> commentDtoGeneralResponse = new GeneralResponse<>();
        commentDtoGeneralResponse.setItems(commentDtos);
        JsonNode nodeComment = mapper.valueToTree(commentDtoGeneralResponse);

        wireMockServer.stubFor(WireMock.get(urlEqualTo(testUriComment))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(nodeComment)));
        wireMockServer.stubFor(WireMock.post(urlEqualTo(testUrlClient)).willReturn(aResponse().withStatus(200)
            .withHeader(HttpHeaders.CONNECTION, "close")));
    }

    @Test
    @Transactional
    @Rollback
    void update() {
        chatRepository.createChat(1);
        LinkEntity linkEntity = linkRepository.add("https://stackoverflow.com/questions/57626072/connection-refused-when-using-wiremock");
        chatLinkRepository.create(1, 1);
        StackUpdater stackUpdater = new StackUpdater(stackOverflowClient,
            commentRepository,
            answerRepository,
            chatLinkRepository,
            linkRepository,
            botClient);

        stackUpdater.update(linkEntity);

        wireMockServer.verify(getRequestedFor(urlEqualTo(testUriAnswer)));
        wireMockServer.verify(getRequestedFor(urlEqualTo(testUriComment)));
        wireMockServer.verify(postRequestedFor(urlEqualTo(testUrlClient)));
        Assertions.assertEquals(2, commentRepository.getAllByLinkId(linkEntity.getId()).size());
        Assertions.assertEquals(2, answerRepository.getAllByLinkId(linkEntity.getId()).size());
    }
}
