package edu.java.bot.model;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.CommandList;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.listener.BotUpdateListener;
import edu.java.bot.service.LinkService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class Bot extends TelegramBot implements AutoCloseable {
    @Autowired
    public Bot(ApplicationConfig applicationConfig, LinkService linkService) {
        super(applicationConfig.telegramToken());
        CommandList commandList = new CommandList(this, linkService);
        setUpdatesListener(new BotUpdateListener(commandList));
        WebClient client = WebClient.builder()
            .baseUrl(String.format("https://api.telegram.org/bot%s", applicationConfig.telegramToken()))
            .build();
        Map<String, List<Command>> map = new HashMap<>();
        map.put("commands", new ArrayList<>(commandList.getCommandMap().values()));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"commands\":[");
        for (Command command : commandList.getCommandMap().values()) {
            stringBuilder.append("{\"command\":\"").append(command.getName()).append("\",\n");
            stringBuilder.append("\"description\":\"").append(command.getDescription()).append("\" \n},");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("]}");
        client.post()
            .uri("/setmycommands")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(stringBuilder.toString())
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    public SendResponse sendMessage(long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        return execute(sendMessage);
    }

    @Override
    public void close() throws Exception {
        super.shutdown();
    }
}
