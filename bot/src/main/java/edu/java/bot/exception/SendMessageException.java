package edu.java.bot.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SendMessageException extends RuntimeException {
    private int errorCode;
    private String description;
}
