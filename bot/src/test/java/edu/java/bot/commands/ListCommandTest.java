package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import edu.java.bot.model.Bot;
import edu.java.bot.model.Link;
import edu.java.bot.service.LinkService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ListCommandTest {
    Bot bot;
    Update update;
    Long chatId = 1234567824356L;

    @BeforeEach
    void init() {
        bot = Mockito.mock(Bot.class);
        update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        Mockito.when(message.text()).thenReturn("/list");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(chatId);
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.from()).thenReturn(new User(123L));
    }

    @Test
    void testLinks() {
        //given
        List<Link> expected = List.of(new Link("http://github.com"), new Link("http://test.com"));
        var command = new ListCommand(bot, new LinkService(expected));

        //when
        command.execute(update);

        //then
        Mockito.verify(bot).sendMessage(chatId, expected.toString());
    }

    @Test
    void testNoLinks() {
        //given
        List<Link> list = new ArrayList<>();
        var command = new ListCommand(bot, new LinkService(list));

        //when
        command.execute(update);

        //then
        Mockito.verify(bot).sendMessage(chatId, ListCommand.NO_LINKS);
    }
}
