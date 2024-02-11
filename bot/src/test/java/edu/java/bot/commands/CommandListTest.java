package edu.java.bot.commands;

import edu.java.bot.model.Bot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class CommandListTest {
    static CommandList commandList;
    @BeforeAll
    public static void init() {
        commandList = new CommandList(null,null);
    }

    @Test
    void getAll() {
        commandList.getCommandMap().values()
            .forEach(val -> {
                Command command = commandList.get("/"+val.getName());
                Assertions.assertNotEquals(NoCommand.class, command.getClass());
            });
    }

    @Test
    void getNoCommand() {
        Command command=commandList.get("/sdfdfds");

        Assertions.assertEquals(command.getClass(), NoCommand.class);
    }
}
