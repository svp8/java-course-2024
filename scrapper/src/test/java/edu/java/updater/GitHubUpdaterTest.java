package edu.java.updater;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.client.BotClient;
import edu.java.client.GithubClientImpl;
import edu.java.dto.github.BranchDto;
import edu.java.dto.github.PullRequestDto;
import edu.java.entity.LinkEntity;
import edu.java.repository.ChatLinkRepository;
import edu.java.repository.ChatRepository;
import edu.java.repository.LinkRepository;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.BranchService;
import edu.java.service.ChatService;
import edu.java.service.LinkService;
import edu.java.service.MessageService;
import edu.java.service.PullService;
import java.util.List;
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
import wiremock.com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@SpringBootTest
class GitHubUpdaterTest extends IntegrationTest {
    @Autowired
    private PullService pullRepository;
    @Autowired
    private BranchService branchRepository;
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
    private static  GithubClientImpl githubClient;
    private static BotClient botClient ;
    static MessageService messageService;
    static String testUrlPull = "/repos/svp8/java-course-2024/pulls";
    static String testUrlBranch = "/repos/svp8/java-course-2024/branches";
    static String testUrlClient = "/send";

    @BeforeAll
    static void init() {
        wireMockServer.start();
        githubClient=new GithubClientImpl(wireMockServer.baseUrl(), WebClient.builder(), Retry.max(100));
        botClient = new BotClient(wireMockServer.baseUrl(), WebClient.builder(),Retry.max(100));
        List<BranchDto> expected = List.of(new BranchDto("test"), new BranchDto("test2"));
        ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
        JsonNode node = mapper.valueToTree(expected);

        wireMockServer.stubFor(WireMock.get(urlEqualTo(testUrlBranch))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withHeader(HttpHeaders.CONNECTION, "close")
                .withJsonBody(node)));
        List<PullRequestDto> pullRequestDtos = List.of(new PullRequestDto(1, "test"), new PullRequestDto(2, "test"));

        JsonNode node2 = mapper.valueToTree(pullRequestDtos);

        wireMockServer.stubFor(WireMock.get(urlEqualTo(testUrlPull))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json;charset=UTF-8")
                .withJsonBody(node2)));

        wireMockServer.stubFor(WireMock.post(urlEqualTo(testUrlClient)).willReturn(aResponse().withStatus(200)
            .withHeader(HttpHeaders.CONNECTION, "close")));
        messageService=new MessageService(null,botClient,false);
    }

    @AfterAll
    static void end() {
        wireMockServer.stop();
    }

//    @BeforeEach
//    void setUpData() {
//
//
//
//    }

    @Test
    @Transactional
    @Rollback
    void update() {
        chatRepository.createChat(1);
        LinkEntity linkEntity = linkRepository.add("https://github.com/svp8/java-course-2024");
        chatLinkRepository.create(1, linkEntity.getId());
        GitHubUpdater gitHubUpdater = new GitHubUpdater(githubClient,
            pullRepository,
            branchRepository,
            chatService,
            messageService, linkService
        );

        gitHubUpdater.update(linkEntity);

        wireMockServer.verify(getRequestedFor(urlEqualTo(testUrlBranch)));
        wireMockServer.verify(getRequestedFor(urlEqualTo(testUrlPull)));
        wireMockServer.verify(postRequestedFor(urlEqualTo(testUrlClient)));
        Assertions.assertEquals(2, pullRepository.getAllByLinkId(linkEntity.getId()).size());
        Assertions.assertEquals(2, branchRepository.getAllByLinkId(linkEntity.getId()).size());
    }
}
