package edu.java.bot.controller;

import edu.java.bot.exception.SendMessageException;
import edu.java.bot.model.request.UpdateRequest;
import edu.java.bot.service.UpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BotController {
    private final UpdateService updateService;

    public BotController(UpdateService updateService) {
        this.updateService = updateService;
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
        updateService.sendUpdates(updateRequest);
        return ResponseEntity.ok().build();
    }

}
