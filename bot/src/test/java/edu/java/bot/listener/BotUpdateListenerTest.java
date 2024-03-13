package edu.java.bot.listener;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import edu.java.bot.commands.CommandList;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.model.Bot;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class BotUpdateListenerTest {
    Bot bot;
    Update startUpdate;
    Long chatId = 1234567824356L;

    @BeforeEach
    void init() {
        bot = Mockito.mock(Bot.class);
        startUpdate = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        Mockito.when(message.text()).thenReturn("/start");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(chatId);
        Mockito.when(startUpdate.message()).thenReturn(message);
        Mockito.when(message.from()).thenReturn(new User(123L));
    }

    @Test
    @DisplayName("Should be not in dialog and execute command")
    void process() {
        CommandList commandList = Mockito.mock(CommandList.class);
        StartCommand startCommand = Mockito.mock(StartCommand.class);
        Mockito.when(commandList.get("/start")).thenReturn(startCommand);
        BotUpdateListener botUpdateListener = new BotUpdateListener(commandList, bot);
        //when
        int result = botUpdateListener.process(List.of(startUpdate));
        //then
        Assertions.assertEquals(result, UpdatesListener.CONFIRMED_UPDATES_ALL);
        Mockito.verify(startCommand).execute(startUpdate, false);
    }

    @Test
    @DisplayName("Should be in dialog and execute command")
    void checkDialog() {
        CommandList commandList = Mockito.mock(CommandList.class);
        StartCommand startCommand = Mockito.mock(StartCommand.class);
        Mockito.when(commandList.get("/start")).thenReturn(startCommand);

        BotUpdateListener botUpdateListener = new BotUpdateListener(commandList, bot);

        Update textUpdate = Mockito.mock(Update.class);
        Message message = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        Mockito.when(message.text()).thenReturn("text");
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(chat.id()).thenReturn(chatId);
        Mockito.when(textUpdate.message()).thenReturn(message);
        //when
        int result = botUpdateListener.process(List.of(startUpdate));
        result = botUpdateListener.process(List.of(textUpdate));
        //then
        Assertions.assertEquals(result, UpdatesListener.CONFIRMED_UPDATES_ALL);
        Mockito.verify(startCommand).execute(textUpdate, true);
    }
}
