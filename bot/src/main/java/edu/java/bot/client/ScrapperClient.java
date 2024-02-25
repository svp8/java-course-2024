package edu.java.bot.client;

import edu.java.bot.exception.ScrapperException;
import edu.java.bot.exception.SendMessageException;
import edu.java.bot.model.Link;
import edu.java.bot.model.request.TrackLinkRequest;
import edu.java.bot.model.request.UntrackLinkRequest;
import edu.java.bot.model.response.LinkListResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public class ScrapperClient {
    public static final String BASE_URL = "http://localhost:8080";
    private final WebClient webClient;

    public ScrapperClient(String baseUrl) {
        if (baseUrl == null || baseUrl.isBlank()) {
            this.webClient = WebClient.create(BASE_URL);
        } else {
            this.webClient = WebClient.create(baseUrl);
        }
    }

    public ScrapperClient() {
        this.webClient = WebClient.create(BASE_URL);
    }

    public Link trackLink(long chatId, String link) {
        WebClient.ResponseSpec responseSpec = webClient
            .post()
            .uri("/track")
            .body(Mono.just(new TrackLinkRequest(chatId, link)), TrackLinkRequest.class)
            .retrieve();
        Link created = responseSpec.bodyToMono(Link.class).block();
        return created;
    }

    public String untrackLink(long chatId, String link) {
        WebClient.ResponseSpec responseSpec = webClient
            .post()
            .uri("/untrack")
            .body(Mono.just(new UntrackLinkRequest(chatId, link)), UntrackLinkRequest.class)
            .retrieve();
        try {
            responseSpec.toBodilessEntity().onErrorComplete().block();
            return "untracked";
        } catch (WebClientResponseException e) {
            ScrapperException scrapperException =e.getResponseBodyAs(ScrapperException.class);
            System.out.println(scrapperException.getDescription());
            return scrapperException.getDescription();
        }
    }

    public LinkListResponse getLinkList(long chatId) {
        WebClient.ResponseSpec responseSpec = webClient
            .get()
            .uri(String.format("/%d/all", chatId))
            .retrieve();
        try {
            LinkListResponse response = responseSpec.bodyToMono(LinkListResponse.class).block();
            return response;
        } catch (WebClientResponseException e) {
            ScrapperException scrapperException =e.getResponseBodyAs(ScrapperException.class);
            System.out.println(scrapperException.getDescription());
            return null;
        }
    }

    public String registerChat(long chatId) {
        WebClient.ResponseSpec responseSpec = webClient
            .post()
            .uri(String.format("/register/%d", chatId))
            .retrieve();
        try {
            responseSpec.toBodilessEntity().block();
            return "registered ";
        } catch (WebClientResponseException e) {
            ScrapperException scrapperException =e.getResponseBodyAs(ScrapperException.class);
            System.out.println(scrapperException.getDescription());
            return scrapperException.getDescription();
        }
    }
}
