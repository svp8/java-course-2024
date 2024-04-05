package edu.java.service;

import edu.java.scrapper.IntegrationTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class KafkaIntegrationTest  extends IntegrationTest {
    public static KafkaContainer kafka;

    static {
        kafka =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));
        kafka.start();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }
}
