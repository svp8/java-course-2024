package edu.java.bot.model;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Link {
    private URI uri;

    public Link() {
    }

    @Override public String toString() {
        return "Link{"
            + "name='" + uri + '\'' + '}';
    }
}
