package edu.java.bot.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Link {
    private String name;

    @Override public String toString() {
        return "Link{"
            + "name='" + name + '\'' + '}';
    }
}
