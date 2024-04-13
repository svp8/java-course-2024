package edu.java.bot.client;

import edu.java.bot.model.Link;
import edu.java.bot.model.request.TrackLinkRequest;
import edu.java.bot.model.request.UntrackLinkRequest;
import edu.java.bot.model.response.LinkListResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class ScrapperClient {
    public static final String BASE_URL = "http://localhost:8080";
    private final WebClient webClient;
    private final Retry retry;

    public ScrapperClient(String baseUrl, WebClient.Builder builder, Retry retry) {
        this.webClient = builder.baseUrl(ClientUtils.getBaseUrl(baseUrl, BASE_URL)).build();
        this.retry = retry;
    }

    public ScrapperClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(BASE_URL).build();
        this.retry = Retry.max(0);
    }

    public Link trackLink(long chatId, String link) throws WebClientResponseException {
        WebClient.ResponseSpec responseSpec = webClient
            .post()
            .uri("/track")
            .body(Mono.just(new TrackLinkRequest(chatId, link)), TrackLinkRequest.class)
            .retrieve();
        Link created = responseSpec.bodyToMono(Link.class).retryWhen(retry).block();
        return created;
    }

    public void untrackLink(long chatId, String link) throws WebClientResponseException {
        WebClient.ResponseSpec responseSpec = webClient
            .post()
            .uri("/untrack")
            .body(Mono.just(new UntrackLinkRequest(chatId, link)), UntrackLinkRequest.class)
            .retrieve();
        responseSpec.toBodilessEntity().retryWhen(retry).block();
    }

    public LinkListResponse getLinkList(long chatId) throws WebClientResponseException {
        WebClient.ResponseSpec responseSpec = webClient
            .get()
            .uri(String.format("/%d/all", chatId))
            .retrieve();
        LinkListResponse response = responseSpec.bodyToMono(LinkListResponse.class).retryWhen(retry).block();
        return response;

    }

    public void registerChat(long chatId) throws WebClientResponseException {
        WebClient.ResponseSpec responseSpec = webClient
            .post()
            .uri(String.format("/register/%d", chatId))
            .retrieve();
        responseSpec.toBodilessEntity().retryWhen(retry).block();

    }
}
