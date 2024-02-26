package edu.java.bot.listener;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import edu.java.bot.commands.CommandList;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.NoCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.model.Bot;
import edu.java.bot.service.LinkService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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
    void process() {
        Update listUpdate = Mockito.mock(Update.class);
        Message listMessage = Mockito.mock(Message.class);
        Chat chat = Mockito.mock(Chat.class);
        Mockito.when(listMessage.text()).thenReturn("/list");
        Mockito.when(listMessage.chat()).thenReturn(chat);
        Mockito.when(listMessage.chat().id()).thenReturn(chatId);
        Mockito.when(listUpdate.message()).thenReturn(listMessage);
        Mockito.when(listMessage.from()).thenReturn(new User(123L));

        BotUpdateListener botUpdateListener = new BotUpdateListener(new CommandList(List.of(new StartCommand(bot)
            , new ListCommand(bot, new LinkService(new ArrayList<>()))), new NoCommand(bot)), bot
        );
        int result = botUpdateListener.process(List.of(listUpdate, startUpdate));
        Assertions.assertEquals(result, UpdatesListener.CONFIRMED_UPDATES_ALL);
        Mockito.verify(bot, Mockito.atLeast(2))
            .sendMessage(ArgumentMatchers.any(long.class), ArgumentMatchers.any(String.class));
    }
}
