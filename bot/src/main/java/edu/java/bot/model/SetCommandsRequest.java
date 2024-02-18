package edu.java.bot.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SetCommandsRequest {
    List<CommandDto> commands;

    record CommandDto(String command, String description) {
    }
}
