package edu.java.bot.client;

import edu.java.bot.model.Link;
import edu.java.bot.model.request.TrackLinkRequest;
import edu.java.bot.model.request.UntrackLinkRequest;
import edu.java.bot.model.response.LinkListResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public class ScrapperClient {
    public static final String BASE_URL = "http://localhost:8080";
    private final WebClient webClient;

    public ScrapperClient(String baseUrl,WebClient.Builder builder) {
        if (baseUrl == null || baseUrl.isBlank()) {
            this.webClient = builder.baseUrl(BASE_URL).build();
        } else {
            this.webClient = WebClient.create(baseUrl);
        }
    }

    public ScrapperClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(BASE_URL).build();
    }

    public Link trackLink(long chatId, String link) throws WebClientResponseException {
        WebClient.ResponseSpec responseSpec = webClient
            .post()
            .uri("/track")
            .body(Mono.just(new TrackLinkRequest(chatId, link)), TrackLinkRequest.class)
            .retrieve();
        Link created = responseSpec.bodyToMono(Link.class).block();
        return created;
    }

    public void untrackLink(long chatId, String link) throws WebClientResponseException {
        WebClient.ResponseSpec responseSpec = webClient
            .post()
            .uri("/untrack")
            .body(Mono.just(new UntrackLinkRequest(chatId, link)), UntrackLinkRequest.class)
            .retrieve();
        responseSpec.toBodilessEntity().block();
    }

    public LinkListResponse getLinkList(long chatId) throws WebClientResponseException {
        WebClient.ResponseSpec responseSpec = webClient
            .get()
            .uri(String.format("/%d/all", chatId))
            .retrieve();
        LinkListResponse response = responseSpec.bodyToMono(LinkListResponse.class).block();
        return response;

    }

    public void registerChat(long chatId) throws WebClientResponseException {
        WebClient.ResponseSpec responseSpec = webClient
            .post()
            .uri(String.format("/register/%d", chatId))
            .retrieve();
        responseSpec.toBodilessEntity().block();

    }
}
