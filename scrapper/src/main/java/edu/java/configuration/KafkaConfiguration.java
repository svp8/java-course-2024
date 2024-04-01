package edu.java.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfiguration {
    @Value("${app.kafka.topic}")
    private String topic;
    @Value("${app.kafka.topic}")
    private Integer replicas;
    @Value("${app.kafka.topic}")
    private Integer partitions;

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(topic)
            .partitions(partitions)
            .replicas(replicas)
            .build();
    }
}
