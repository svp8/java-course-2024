package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import edu.java.bot.model.Bot;
import edu.java.bot.model.CommandType;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
        String expected = stringBuilder.toString();
        //when
        command.execute(update, false);

        //then
        Mockito.verify(bot).sendMessage(chatId, expected);
    }
}
