package edu.java.bot.commands;

import java.util.List;
import java.util.Optional;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CommandList {
    @Autowired
    private List<Command> commandList;
    @Autowired
    private NoCommand noCommand;

    public Command get(String name) {
        Optional<Command> command = commandList.stream().filter(x -> x.getType().getName().equals(name)).findAny();
        return command.orElse(noCommand);
    }
}
