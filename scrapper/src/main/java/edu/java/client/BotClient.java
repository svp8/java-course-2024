package edu.java.client;

import edu.java.dto.request.UpdateRequest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotClient {
    public static final String BASE_URL = "http://localhost/8080";
    private final WebClient webClient;

    public BotClient(String baseUrl, WebClient.Builder builder) {
        this.webClient = builder.baseUrl(ClientUtils.getBaseUrl(baseUrl, BASE_URL)).build();
    }

    public BotClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(BASE_URL).build();
    }

    public void sendUpdate(UpdateRequest updateRequest) {
        WebClient.ResponseSpec responseSpec = webClient
            .post()
            .uri("/send")
            .body(Mono.just(updateRequest), UpdateRequest.class)
            .retrieve();
        responseSpec.toBodilessEntity().block();
    }
}
