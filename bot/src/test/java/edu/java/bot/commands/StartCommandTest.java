package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.exception.ScrapperException;
import edu.java.bot.model.Bot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClientResponseException;

class StartCommandTest {

    Bot bot;
    ScrapperClient scrapperClient;
    Update update;
    Long chatId = 1234567824356L;

    @BeforeEach
    void init() {
        scrapperClient = Mockito.mock(ScrapperClient.class);
        bot = Mockito.mock(Bot.class);
        update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);

        Mockito.when(message.text()).thenReturn("/start");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(chatId);
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.from()).thenReturn(new User(123L));
    }

    @Test
    void testStart() {
        //given
        var command = new StartCommand(bot, scrapperClient);
        //when
        command.execute(update, false);

        //then
        Mockito.verify(bot).sendMessage(chatId, StartCommand.START_MESSAGE);
    }

    @Test
    void testChatAlreadyRegistered() {
        //given
        ScrapperException expected = new ScrapperException(403, "Exception");
        WebClientResponseException webClientResponseException = Mockito.mock(WebClientResponseException.class);
        Mockito.when(webClientResponseException.getResponseBodyAs(ScrapperException.class)).thenReturn(expected);
        Mockito.doThrow(webClientResponseException).when(scrapperClient).registerChat(chatId);
        var command = new StartCommand(bot, scrapperClient);
        //when
        command.execute(update, false);

        //then
        Mockito.verify(bot).sendMessage(chatId, expected.getDescription());
    }
}
