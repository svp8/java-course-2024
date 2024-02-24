package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import edu.java.bot.model.Bot;
import edu.java.bot.model.CommandType;
import edu.java.bot.model.Link;
import edu.java.bot.service.LinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class HelpCommandTest {
    Bot bot;
    Update update;
    Long chatId = 1234567824356L;

    @BeforeEach
    void init() {
        bot = Mockito.mock(Bot.class);
        update = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);

        Mockito.when(message.text()).thenReturn("/help");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(chatId);
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.from()).thenReturn(new User(123L));
    }
    @Test
    void testSendMessage() {
        //given
        var command = new HelpCommand(bot);
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(CommandType.values())
            .filter(commandType -> !commandType.equals(CommandType.NO))
            .forEach((commandType) -> stringBuilder
                .append(commandType.getName())
                .append(" - ")
                .append(commandType.getDescription())
                .append("\n"));
        String expected=stringBuilder.toString();
        //when
        command.execute(update);

        //then
        Mockito.verify(bot).sendMessage(chatId, expected);
    }
}