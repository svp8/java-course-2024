package edu.java.bot.configuration;

import edu.java.bot.client.ScrapperClient;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken
) {
    @Bean
    public ScrapperClient scrapperClient(@Value("client.scrapper.baseurl") String baseUrl) {
        return new ScrapperClient();
    }
}
