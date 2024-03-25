package edu.java.bot.model.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SetCommandsRequest {
    List<CommandDto> commands;

    public record CommandDto(String command, String description) {
    }
}
