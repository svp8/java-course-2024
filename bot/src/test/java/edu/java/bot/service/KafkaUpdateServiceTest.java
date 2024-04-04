package edu.java.bot.service;

import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.model.Bot;
import edu.java.bot.model.Link;
import edu.java.bot.model.scrapper.Chat;
import edu.java.bot.model.scrapper.Update;
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
import org.mockito.Mockito;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

@SpringBootTest
public class KafkaUpdateServiceTest extends KafkaIntegrationTest {
    @Autowired KafkaTemplate<String, Update> kafkaTemplate;
    @Autowired KafkaUpdateService updateService;
    @MockBean Bot bot;
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    private String dlq = "dlq";

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
    void sendUpdateKafka() {
        SendResponse sendResponse = Mockito.mock(SendResponse.class);
        Update update = new Update(new Chat(1), new Link(URI.create("123")), List.of());
        Mockito.when(bot.sendMessage(1, UpdateService.formatUpdates(update.link(), update.linkUpdates())))
            .thenReturn(sendResponse);
        Mockito.when(sendResponse.isOk()).thenReturn(true);
        kafkaTemplate.send("update", update);
        await()
            .pollInterval(Duration.ofSeconds(3))
            .atMost(12, SECONDS)
            .untilAsserted(() -> {

                Mockito.verify(bot).sendMessage(1, UpdateService.formatUpdates(update.link(), update.linkUpdates()));
            });
    }

    @Test
    void sendUpdateKafkaDLQ() {

        SendResponse sendResponse = Mockito.mock(SendResponse.class);
        Update update = new Update(new Chat(1), new Link(URI.create("123")), List.of());
        Mockito.when(bot.sendMessage(1, UpdateService.formatUpdates(update.link(), update.linkUpdates())))
            .thenReturn(sendResponse);
        Mockito.when(sendResponse.isOk()).thenReturn(false);
        Mockito.when(sendResponse.description()).thenReturn("error");
        Mockito.when(sendResponse.errorCode()).thenReturn(123);
        kafkaTemplate.send("update", update);
        try(KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(getConsumerProps())){
            kafkaConsumer.subscribe(List.of(dlq));
            Unreliables.retryUntilTrue(10, TimeUnit.SECONDS, () -> {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
                if (records.isEmpty()) {
                    return false;
                }
                for (ConsumerRecord<String, String> record : records) {
                    Assertions.assertEquals("edu.java.bot.exception.SendMessageException", record.value());
                }
                return true;
            });
        }
    }
}
