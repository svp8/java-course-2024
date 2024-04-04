package edu.java.service;

import edu.java.dto.Chat;
import edu.java.dto.Link;
import edu.java.dto.Update;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rnorth.ducttape.unreliables.Unreliables;
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
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put("properties.spring.json.trusted.packages", "*");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return props;
    }

    @Test
    void sendUpdate() {
        Update update = new Update(new Chat(1), new Link(URI.create("123")), List.of());
        messageService.sendUpdate(update);
        try(KafkaConsumer<String, Update> kafkaConsumer = new KafkaConsumer<>(getConsumerProps())){
            kafkaConsumer.subscribe(List.of(topic));
            Unreliables.retryUntilTrue(10, TimeUnit.SECONDS, () -> {
                ConsumerRecords<String, Update> records = kafkaConsumer.poll(Duration.ofMillis(100));
                if (records.isEmpty()) {
                    return false;
                }
                for (ConsumerRecord<String, Update> record : records) {
                    Assertions.assertEquals(update, record.value());
                }
                return true;
            });
        }

    }
}
