package edu.java.client;

import edu.java.dto.Update;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class BotClient {
    public static final String BASE_URL = "http://localhost/8080";
    private final WebClient webClient;
    private final Retry retry;

    public BotClient(String baseUrl, WebClient.Builder builder, Retry retry) {
        this.retry = retry;
        this.webClient = builder.baseUrl(ClientUtils.getBaseUrl(baseUrl, BASE_URL)).build();
    }

    public BotClient(WebClient.Builder builder) {
        this.retry = Retry.max(0);
        this.webClient = builder.baseUrl(BASE_URL).build();
    }

    public void sendUpdate(Update update) {
        WebClient.ResponseSpec responseSpec = webClient
            .post()
            .uri("/send")
            .body(Mono.just(update), Update.class)
            .retrieve();
        responseSpec.toBodilessEntity().retryWhen(retry).block();
    }
}
