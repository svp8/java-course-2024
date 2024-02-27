package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.exception.ScrapperException;
import edu.java.bot.model.Bot;
import edu.java.bot.model.Link;
import edu.java.bot.model.response.LinkListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

class UntrackCommandTest {
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
        Mockito.when(message.from()).thenReturn(new User(123L));
    }
    @Test
    void testWrongNumber() throws URISyntaxException {
        //given
        List<Link> linkList = List.of(new Link(new URI("http://github.com")), new Link(new URI("http://test.com")));
        Mockito.when(scrapperClient.getLinkList(chatId)).thenReturn(new LinkListResponse(linkList,0));

        var command = new UntrackCommand(bot, scrapperClient);
        //when
        command.execute(update, false);

        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        Mockito.when(message.text()).thenReturn("10");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(chatId);
        Mockito.when(update.message()).thenReturn(message);

        command.execute(update, true);

        //then
        Mockito.verify(bot).sendMessage(chatId, UntrackCommand.NO_SUCH_LINK_NUMBER);
    }
    @Test
    void testWrongInput() throws URISyntaxException {
        //given
        List<Link> linkList = List.of(new Link(new URI("http://github.com")), new Link(new URI("http://test.com")));
        Mockito.when(scrapperClient.getLinkList(chatId)).thenReturn(new LinkListResponse(linkList,0));

        var command = new UntrackCommand(bot, scrapperClient);
        //when
        command.execute(update, false);

        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        Mockito.when(message.text()).thenReturn("l");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(chatId);
        Mockito.when(update.message()).thenReturn(message);

        command.execute(update, true);

        //then
        Mockito.verify(bot).sendMessage(chatId, UntrackCommand.WRONG_INPUT_FORMAT);
    }

    @Test
    void testExecute() throws URISyntaxException {
        //given
        List<Link> expected = List.of(new Link(new URI("http://github.com")), new Link(new URI("http://test.com")));
        Mockito.when(scrapperClient.getLinkList(chatId)).thenReturn(new LinkListResponse(expected,0));
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < expected.size(); i++) {
            stringBuilder.append((i + 1))
                .append(". ")
                .append(expected.get(i)
                    .getUri()
                    .toString())
                .append("\n");
        }
        var command = new UntrackCommand(bot, scrapperClient);
        stringBuilder.append("Choose number of link to untrack");
        command.execute(update, false);



        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        Mockito.when(message.text()).thenReturn("1");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(chatId);
        Mockito.when(update.message()).thenReturn(message);
        //when
        command.execute(update, true);

        //then
        Mockito.verify(bot,Mockito.atLeastOnce()).sendMessage(chatId, stringBuilder.toString());
        Mockito.verify(bot,Mockito.atLeastOnce()).sendMessage(chatId, "1 untracked");

    }
}
