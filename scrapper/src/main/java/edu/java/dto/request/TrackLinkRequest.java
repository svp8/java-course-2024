package edu.java.dto.request;

import edu.java.dto.Link;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TrackLinkRequest {
    private long chatId;
    private String link;
}
