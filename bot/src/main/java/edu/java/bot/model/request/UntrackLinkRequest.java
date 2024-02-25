package edu.java.bot.model.request;

import edu.java.bot.model.Link;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UntrackLinkRequest {
    private long chatId;
    private String linkId;
}
