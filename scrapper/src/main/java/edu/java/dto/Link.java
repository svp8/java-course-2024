package edu.java.dto;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

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
