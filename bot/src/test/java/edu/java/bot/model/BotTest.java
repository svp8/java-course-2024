package edu.java.bot.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class BotTest {
    Bot bot;
    Long chatId = 1234567824356L;

    @BeforeEach
    void init() {
        bot = Mockito.mock(Bot.class);
    }

    @Test
    void sendMessage() {
        bot.sendMessage(chatId, "test");

        Mockito.verify(bot).sendMessage(chatId, "test");
    }
}
