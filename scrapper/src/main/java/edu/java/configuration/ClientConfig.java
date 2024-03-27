package edu.java.configuration;

import edu.java.client.BotClient;
import edu.java.client.GitHubClient;
import edu.java.client.GithubClientImpl;
import edu.java.client.StackOverflowClient;
import edu.java.client.StackOverflowClientImpl;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

@Validated
@ConfigurationProperties(prefix = "client", ignoreUnknownFields = false)
public record ClientConfig(@NotNull GitHub gitHub,
                           @NotNull StackOverflow stackOverflow,
                           @NotNull Bot bot) {

    @Bean
    public BotClient botClient(
        WebClient.Builder builder
    ) {
        Retry retry = RetryUtil.getRetry(bot.retry);
        return new BotClient(gitHub.baseUrl, builder, retry);
    }

    @Bean
    public GitHubClient gitHubClient(WebClient.Builder builder) {
        Retry retry = RetryUtil.getRetry(gitHub.retry);
        return new GithubClientImpl(gitHub.baseUrl, builder, retry);
    }

    @Bean
    public StackOverflowClient stackOverflowClient(
        WebClient.Builder builder
    ) {
        Retry retry = RetryUtil.getRetry(stackOverflow.retry);
        return new StackOverflowClientImpl(stackOverflow.baseUrl, builder, retry);
    }

    public record RetryConfig(
        boolean enable,
        RetryType type,
        int maxAttempts, // default 100
        int baseDelayMs, // default 1000 ms
        Set<Integer> retryOnCodes // default any non 2xx
    ) {
    }

    public enum RetryType {
        constant, linear, exponential
    }

    public record GitHub(@NotEmpty String token, String baseUrl, RetryConfig retry) {
    }

    public record StackOverflow(String baseUrl, RetryConfig retry) {
    }

    public record Bot(@NotEmpty String baseUrl, RetryConfig retry) {
    }
}
