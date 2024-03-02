package edu.java.configuration;

import edu.java.client.GitHubClient;
import edu.java.client.GithubClientImpl;
import edu.java.client.StackOverflowClient;
import edu.java.client.StackOverflowClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApplicationConfig {
    @Bean
    public GitHubClient gitHubClient(@Value("${client.github.baseurl}") String baseUrl, WebClient.Builder builder) {
        return new GithubClientImpl(baseUrl, builder);
    }

    @Bean
    public StackOverflowClient stackOverflowClient(
        @Value("${client.stack.baseurl}") String baseUrl,
        WebClient.Builder builder
    ) {
        return new StackOverflowClientImpl(baseUrl, builder);
    }
}
