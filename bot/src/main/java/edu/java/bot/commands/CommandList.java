package edu.java.bot.commands;

import java.util.List;
import java.util.Optional;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CommandList {

    private final List<Command> commandList;

    private final NoCommand noCommand;

    public CommandList(List<Command> commandList, NoCommand noCommand) {
        this.commandList = commandList;
        this.noCommand = noCommand;
    }

    public Command get(String name) {
        Optional<Command> command = commandList.stream().filter(x -> x.getType().getName().equals(name)).findAny();
        return command.orElse(noCommand);
    }
}
