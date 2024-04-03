package edu.java.service;

import edu.java.dto.Chat;
import edu.java.dto.Link;
import edu.java.dto.Update;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@SpringBootTest
class ScrapperQueueProducerTest extends KafkaIntegrationTest {
    @Autowired
    MessageService messageService;
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${app.kafka.topic}")
    private String topic;

    private Map<String, Object> getConsumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return props;
    }

    @Test
    void sendUpdate() {

        try (KafkaConsumer<String, Update> kafkaConsumer = new KafkaConsumer<>(getConsumerProps())) {
            kafkaConsumer.subscribe(List.of(topic));
            Update update = new Update(new Chat(1), new Link(URI.create("123")), List.of());
            messageService.sendUpdate(update);
            Iterable<ConsumerRecord<String, Update>> records = kafkaConsumer.poll(Duration.ofSeconds(5))
                .records(topic);
            for (ConsumerRecord<String, Update> record : records) {
                Assertions.assertEquals(update, record.value());
            }
        }
    }
}
