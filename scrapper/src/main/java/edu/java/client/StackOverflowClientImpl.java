package edu.java.client;

import edu.java.dto.stack.AnswerDto;
import edu.java.dto.stack.CommentDto;
import edu.java.dto.stack.GeneralResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

public class StackOverflowClientImpl implements StackOverflowClient {
    public static final String BASE_URL = "https://api.stackexchange.com/2.3/";
    public static final String SITE_STACKOVERFLOW = "site=stackoverflow";
    private final WebClient webClient;
    private final Retry retry;

    public StackOverflowClientImpl(String baseUrl, WebClient.Builder builder, Retry retry) {
        this.retry = retry;
        this.webClient = builder.baseUrl(ClientUtils.getBaseUrl(baseUrl, BASE_URL)).build();
    }

    public StackOverflowClientImpl(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(BASE_URL).build();
        this.retry = Retry.max(0);
    }

    @Override
    public GeneralResponse<AnswerDto> getAnswersByQuestionId(int id) {
        WebClient.ResponseSpec responseSpec = webClient
            .get()
            .uri(String.format("/questions/%d/answers?%s", id, SITE_STACKOVERFLOW))
            .retrieve();
        GeneralResponse<AnswerDto> response =
            responseSpec.bodyToMono(new ParameterizedTypeReference<GeneralResponse<AnswerDto>>() {
            }).retryWhen(retry).block();
        return response;
    }

    @Override
    public GeneralResponse<CommentDto> getCommentsByQuestionId(int id) {
        WebClient.ResponseSpec responseSpec = webClient
            .get()
            .uri(String.format("/questions/%d/comments?%s", id, SITE_STACKOVERFLOW))
            .retrieve();
        GeneralResponse<CommentDto> response =
            responseSpec.bodyToMono(new ParameterizedTypeReference<GeneralResponse<CommentDto>>() {
            }).retryWhen(retry).block();
        return response;
    }

    @Override
    public String getBaseUrl() {
        return null;
    }
}

