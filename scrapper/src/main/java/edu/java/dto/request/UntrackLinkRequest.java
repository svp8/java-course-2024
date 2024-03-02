package edu.java.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UntrackLinkRequest {
    private long chatId;
    private String linkId;
}
