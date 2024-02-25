package edu.java.bot.controller;

import com.pengrad.telegrambot.response.SendResponse;
import edu.java.bot.exception.SendMessageException;
import edu.java.bot.model.Bot;
import edu.java.bot.model.request.SendMessageRequest;
import edu.java.bot.model.request.UpdateRequest;
import edu.java.bot.model.scrapper.LinkUpdate;
import edu.java.bot.model.scrapper.Update;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class BotController {
    private final Bot bot;

    public BotController(Bot bot) {
        this.bot = bot;
    }

    @PostMapping("/send")
    @Operation(summary = "Send message",
               description = "This method send message to chat")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "ok"),
        @ApiResponse(responseCode = "400", description = "Invalid chat Id", content =
            {@Content(mediaType = "application/json", schema =
            @Schema(implementation = SendMessageException.class))})})
    public ResponseEntity<Void> sendMessage(UpdateRequest updateRequest) {
        for (Update update : updateRequest.updates()) {
            List<LinkUpdate> linkUpdates = update.linkUpdates();
            SendResponse sendResponse = bot.sendMessage(update.chat().getChatId(), formatUpdates(linkUpdates));
            if (!sendResponse.isOk()) {
                throw new SendMessageException(sendResponse.errorCode(), sendResponse.description());
            }
        }
        return ResponseEntity.ok().build();
    }

    public String formatUpdates(List<LinkUpdate> linkUpdates) {
        StringBuilder stringBuilder = new StringBuilder();
        for (LinkUpdate linkUpdate : linkUpdates) {
            stringBuilder.append(linkUpdate.link())
                .append(" - ")
                .append(linkUpdate.link());

        }
        return stringBuilder.toString();
    }
}
