package edu.java.bot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SendMessageException extends RuntimeException {
    private final int errorCode;
    private final String description;
}
