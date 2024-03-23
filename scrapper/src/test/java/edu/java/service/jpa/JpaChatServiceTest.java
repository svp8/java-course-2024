package edu.java.service.jpa;

import edu.java.exception.InvalidChatIdException;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.ChatService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
class JpaChatServiceTest extends IntegrationTest {
    @Autowired ChatService chatService;


    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
    }

    @Test
    @DisplayName("Should throw if already registered")
    void registerChat() {
        chatService.registerChat(123);
        Assertions.assertThrows(InvalidChatIdException.class, () -> chatService.registerChat(123));
    }


}
