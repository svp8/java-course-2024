package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.model.Bot;
import edu.java.bot.model.Link;
import edu.java.bot.model.response.LinkListResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ListCommandTest {
    Bot bot;
    ScrapperClient scrapperClient;
    Update update;
    Long chatId = 1234567824356L;

    @BeforeEach
    void init() {
        scrapperClient=Mockito.mock(ScrapperClient.class);
        bot = Mockito.mock(Bot.class);
        update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        Mockito.when(message.text()).thenReturn("/list");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(chatId);
        Mockito.when(update.message()).thenReturn(message);
    }

    @Test
    void testLinks() throws URISyntaxException {
        //given
        List<Link> expected = List.of(new Link(new URI("http://github.com")), new Link(new URI("http://test.com")));
        Mockito.when(scrapperClient.getLinkList(chatId)).thenReturn(new LinkListResponse(expected,0));
        var command = new ListCommand(bot, scrapperClient);

        //when
        command.execute(update,false );

        //then
        Mockito.verify(bot).sendMessage(chatId, expected.toString());
    }

    @Test
    void testNoLinks() {
        //given
        Mockito.when(scrapperClient.getLinkList(chatId)).thenReturn(new LinkListResponse(null,0));
        var command = new ListCommand( bot, scrapperClient);

        //when
        command.execute(update, false);

        //then
        Mockito.verify(bot).sendMessage(chatId, ListCommand.NO_LINKS);
    }
}
