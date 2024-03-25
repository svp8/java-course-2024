package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.exception.ScrapperException;
import edu.java.bot.model.Bot;
import edu.java.bot.model.Link;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.net.URI;
import java.net.URISyntaxException;

class TrackCommandTest {
    ScrapperClient scrapperClient;
    Bot bot;
    Update update;
    Long chatId = 1234567824356L;

    @BeforeEach
    void init() {
        scrapperClient=Mockito.mock(ScrapperClient.class);
        bot = Mockito.mock(Bot.class);
        update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);

        Mockito.when(message.text()).thenReturn("/track");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(chatId);
        Mockito.when(update.message()).thenReturn(message);
    }
    @Test
    void testThrows() {
        //given
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        Mockito.when(message.text()).thenReturn("/track test");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(chatId);
        Mockito.when(update.message()).thenReturn(message);
        ScrapperException expected = new ScrapperException(403, "Exception");
        WebClientResponseException webClientResponseException = Mockito.mock(WebClientResponseException.class);
        Mockito.when(webClientResponseException.getResponseBodyAs(ScrapperException.class)).thenReturn(expected);
        Mockito.doThrow(webClientResponseException).when(scrapperClient).trackLink(chatId,"test");
        var command = new TrackCommand(bot, scrapperClient);
        //when
        command.execute(update, false);

        //then
        Mockito.verify(bot).sendMessage(chatId, expected.getDescription());
    }

    @Test
    void testExecute() throws URISyntaxException {
        //given
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        Mockito.when(message.text()).thenReturn("/track test");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(chatId);
        Mockito.when(update.message()).thenReturn(message);
        Link expected = new Link(new URI("test"));
        Mockito.when(scrapperClient.trackLink(chatId,"test")).thenReturn(expected);
        var command = new TrackCommand(bot, scrapperClient);
        //when
        command.execute(update, false);

        //then
        Mockito.verify(bot).sendMessage(chatId, "tracking "+expected.getUri().toString());

    }
    @Test
    void testWrongFormat() {
        //given
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        Mockito.when(message.text()).thenReturn("/track");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(chatId);
        Mockito.when(update.message()).thenReturn(message);

        String expected=TrackCommand.FORMAT;
        var command = new TrackCommand(bot, scrapperClient);
        //when
        command.execute(update, false);

        //then
        Mockito.verify(bot).sendMessage(chatId, expected);
    }
}
