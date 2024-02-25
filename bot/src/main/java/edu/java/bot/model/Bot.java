package edu.java.bot.model;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.configuration.ApplicationConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.java.bot.model.request.SetCommandsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class Bot extends TelegramBot implements AutoCloseable {
    @Autowired
    public Bot(ApplicationConfig applicationConfig, WebClient.Builder webClientBuilder) {
        super(applicationConfig.telegramToken());
        WebClient client = webClientBuilder
            .baseUrl(String.format("https://api.telegram.org/bot%s", applicationConfig.telegramToken()))
            .build();
        registerCommands(client);

    }

    private void registerCommands(WebClient client) {
        List<SetCommandsRequest.CommandDto> commandDtos = new ArrayList<>();
        Arrays.stream(CommandType.values()).forEach(commandType -> {
            commandDtos.add(new SetCommandsRequest.CommandDto(commandType.getName(), commandType.getDescription()));
        });
        SetCommandsRequest setCommandsRequest = new SetCommandsRequest(commandDtos);
        client.post()
            .uri("/setmycommands")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(setCommandsRequest), SetCommandsRequest.class)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    public SendResponse sendMessage(long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        return execute(sendMessage);
    }

    @Override
    public void close() {
        super.shutdown();
    }
}
