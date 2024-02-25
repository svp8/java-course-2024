package edu.java.client;

import edu.java.dto.request.UpdateRequest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotClient {
    public static final String BASE_URL = "http://localhost/8080";
    private final WebClient webClient;

    public BotClient(String baseUrl) {
        if (baseUrl == null || baseUrl.isBlank()) {
            this.webClient = WebClient.create(BASE_URL);
        } else {
            this.webClient = WebClient.create(baseUrl);
        }
    }

    public BotClient() {
        this.webClient = WebClient.create(BASE_URL);
    }


    public String sendUpdate(UpdateRequest updateRequest) {
        WebClient.ResponseSpec responseSpec = webClient
            .post()
            .uri("/send")
            .body(Mono.just(updateRequest), UpdateRequest.class)
            .retrieve();
        String response = responseSpec.bodyToMono(String.class).block();
        return response;
    }
}
