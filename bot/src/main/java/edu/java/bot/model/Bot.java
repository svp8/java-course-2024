package edu.java.bot.model;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.CommandList;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.listener.BotUpdateListener;
import edu.java.bot.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Bot {
    private final ApplicationConfig applicationConfig;
    private final TelegramBot bot;

    @Autowired
    public Bot(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
        bot = new TelegramBot(applicationConfig.telegramToken());
        bot.setUpdatesListener(new BotUpdateListener(new CommandList(new MessagingService(this))));
    }

    public void execute(BaseRequest<?,?> baseRequest){
        bot.execute(baseRequest);
    }


}
