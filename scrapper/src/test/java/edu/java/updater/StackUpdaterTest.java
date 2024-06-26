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
import edu.java.scrapper.IntegrationTest;
import edu.java.service.AnswerService;
import edu.java.service.ChatService;
import edu.java.service.CommentService;
import edu.java.service.LinkService;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import edu.java.service.MessageService;
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
import reactor.util.retry.Retry;
import wiremock.com.fasterxml.jackson.databind.JsonNode;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;
import wiremock.com.fasterxml.jackson.databind.PropertyNamingStrategy;
import wiremock.com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static edu.java.client.StackOverflowClientImpl.SITE_STACKOVERFLOW;

@SpringBootTest
class StackUpdaterTest extends IntegrationTest {

    private StackOverflowClient stackOverflowClient =
        new StackOverflowClientImpl(wireMockServer.baseUrl(), WebClient.builder(), Retry.max(1000));

    @AfterAll
    static void end() {
        wireMockServer.stop();
    }

    @Autowired
    private CommentService commentService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private ChatLinkRepository chatLinkRepository;
    @Autowired
    private ChatService chatService;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private LinkService linkService;
    @Autowired
    private ChatRepository chatRepository;

    public static WireMockServer wireMockServer = new WireMockServer(9081);
    private static BotClient botClient ;
    static MessageService messageService;
    static String testUriComment = String.format("/questions/57626072/comments?%s", SITE_STACKOVERFLOW);
    static String testUriAnswer = String.format("/questions/57626072/answers?%s", SITE_STACKOVERFLOW);
    static String testUrlClient = "/send";
    static OffsetDateTime offsetDateTime = OffsetDateTime.of(2000, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC);

    @BeforeAll
    static void init() {
        wireMockServer.start();
        botClient = new BotClient(wireMockServer.baseUrl(), WebClient.builder(),Retry.max(10000));
        AnswerDto[] expectedDto =
            new AnswerDto[] {new AnswerDto(1, true, offsetDateTime, 12, offsetDateTime),
                new AnswerDto(2, true, offsetDateTime, 12, offsetDateTime)};
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
        messageService=new MessageService(null,botClient,false);
    }

    @Test
    @Transactional
    @Rollback
    void update() {
        chatRepository.createChat(1);
        LinkEntity linkEntity =
            linkRepository.add("https://stackoverflow.com/questions/57626072/connection-refused-when-using-wiremock");
        chatLinkRepository.create(1, linkEntity.getId());
        StackUpdater stackUpdater = new StackUpdater(
            stackOverflowClient,

            commentService, answerService, chatService,
            linkService,
            messageService
        );

        stackUpdater.update(linkEntity);

        wireMockServer.verify(getRequestedFor(urlEqualTo(testUriAnswer)));
        wireMockServer.verify(getRequestedFor(urlEqualTo(testUriComment)));
        wireMockServer.verify(postRequestedFor(urlEqualTo(testUrlClient)));
        Assertions.assertEquals(2, commentService.getAllByLinkId(linkEntity.getId()).size());
        Assertions.assertEquals(2, answerService.getAllByLinkId(linkEntity.getId()).size());
    }
}
