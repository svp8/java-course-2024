package edu.java.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import java.net.URI;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Link {
    private URI uri;
//    private long chatId;

    @Override public String toString() {
        return "Link{"
            + "name='" + uri + '\'' + '}';
    }
}
