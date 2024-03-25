package edu.java.bot.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TrackLinkRequest {
    private long chatId;
    private String link;
}
