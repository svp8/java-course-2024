package edu.java.bot.service;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.model.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class MessagingService {
    private final Bot bot;

    public MessagingService(Bot bot) {
        this.bot = bot;
    }

    public void sendMessage(Long chatId,String message){
        SendMessage sendMessage=new SendMessage(chatId,message);
        bot.execute(sendMessage);
    }
}
