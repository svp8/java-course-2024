package edu.java.configuration;

import edu.java.client.BotClient;
import edu.java.client.GitHubClient;
import edu.java.client.GithubClientImpl;
import edu.java.client.StackOverflowClient;
import edu.java.client.StackOverflowClientImpl;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler
) {
    @Bean
    public BotClient scrapperClient(@Value("client.bot.baseurl") String baseUrl) {
        return new BotClient(baseUrl);
    }
    @Bean
    public Scheduler scheduler() {
        return scheduler;
    }

    @Bean
    public GitHubClient gitHubClient(@Value("client.github.baseurl") String baseUrl) {
        return new GithubClientImpl(baseUrl);
    }

    @Bean
    public StackOverflowClient stackOverflowClient(@Value("client.stack.baseurl") String baseUrl) {
        return new StackOverflowClientImpl(baseUrl);
    }

    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }
}
