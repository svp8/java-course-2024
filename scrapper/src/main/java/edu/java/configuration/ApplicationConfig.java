package edu.java.configuration;

import edu.java.client.BotClient;
import edu.java.client.GitHubClient;
import edu.java.client.GithubClientImpl;
import edu.java.client.StackOverflowClient;
import edu.java.client.StackOverflowClientImpl;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApplicationConfig {
    @Bean
    public DefaultConfigurationCustomizer postgresJooqCustomizer() {
        return (DefaultConfiguration c) -> c.settings()
            .withRenderSchema(false)
            .withRenderFormatted(true)
            .withRenderQuotedNames(RenderQuotedNames.NEVER);
    }

//    @Bean
//    public BotClient botClient(@Value("${client.bot.baseurl}") String baseUrl,
//        WebClient.Builder builder
//
//    ) {
//        return new BotClient(baseUrl, builder);
//    }
//
//    @Bean
//    public GitHubClient gitHubClient(@Value("${client.github.baseurl}") String baseUrl, WebClient.Builder builder) {
//        return new GithubClientImpl(baseUrl, builder);
//    }
//
//    @Bean
//    public StackOverflowClient stackOverflowClient(
//        @Value("${client.stack.baseurl}") String baseUrl,
//        WebClient.Builder builder
//    ) {
//        return new StackOverflowClientImpl(baseUrl, builder);
//    }
}
