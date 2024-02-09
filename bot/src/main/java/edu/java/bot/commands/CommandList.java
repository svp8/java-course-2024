package edu.java.bot.commands;

import edu.java.bot.service.MessagingService;
import java.util.Collections;
import java.util.Map;

public class CommandList {
    private final MessagingService messagingService;
    private final Map<String,Command> commandMap;

    public CommandList(MessagingService messagingService) {
        this.messagingService = messagingService;
        this.commandMap= Collections.unmodifiableMap(Map.of(
            "/start",new StartCommand(messagingService)
        ));
    }
    public Command get(String name){
        if(commandMap.containsKey(name)){
            return commandMap.get(name);
        }else {
            return new NoCommand(messagingService);
        }
    }
}
