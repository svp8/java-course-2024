package edu.java.dto;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class Link {
    private URI uri;

    @Override public String toString() {
        return "Link{"
            + "name='" + uri + '\'' + '}';
    }
}
