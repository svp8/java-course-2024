package edu.java.bot.configuration;

import edu.java.bot.client.ScrapperClient;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

@Validated
@ConfigurationProperties(prefix = "client", ignoreUnknownFields = false)
public record ClientConfig(@NotNull Scrapper scrapper) {
    @Bean
    public ScrapperClient scrapperClient(
        @Value("${client.scrapper.baseurl}") String baseUrl,
        WebClient.Builder builder
    ) {
        Retry retry = RetryUtil.getRetry(scrapper.retry);
        return new ScrapperClient(baseUrl, builder, retry);
    }

    public record Scrapper(String baseUrl, RetryConfig retry) {
    }
}
