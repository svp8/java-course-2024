package edu.java.bot.controller;

import edu.java.bot.exception.SendMessageException;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(SendMessageException.class)
    public ResponseEntity<SendMessageException> sendMessageException(SendMessageException sendMessageException){
        return ResponseEntity.status(sendMessageException.getErrorCode()).body(sendMessageException);
    }
}
