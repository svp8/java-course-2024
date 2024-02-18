package edu.java.bot.model;

import lombok.Getter;

@Getter
public enum CommandType {
    START("/start", "register new user"),
    HELP("/help", "show all commands"),
    TRACK("/track", "track link"),
    UNTRACK("/untrack", "untrack link"),
    LIST("/list", "links list"),
    NO("error", "shows on error");

    CommandType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    private final String name;
    private final String description;
}
