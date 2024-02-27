package edu.java.controller;

import edu.java.exception.DuplicateLinkException;
import edu.java.exception.InvalidChatIdException;
import edu.java.exception.NoSuchLinkException;
import edu.java.exception.URIException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(DuplicateLinkException.class)
    public ResponseEntity<DuplicateLinkException> duplicateLinkException(DuplicateLinkException e) {
        return ResponseEntity.status(e.getErrorCode()).body(e);
    }

    @ExceptionHandler(InvalidChatIdException.class)
    public ResponseEntity<InvalidChatIdException> invalidChatIdException(InvalidChatIdException e) {
        return ResponseEntity.status(e.getErrorCode()).body(e);
    }

    @ExceptionHandler(NoSuchLinkException.class)
    public ResponseEntity<NoSuchLinkException> noSuchLinkException(NoSuchLinkException e) {
        return ResponseEntity.status(e.getErrorCode()).body(e);
    }
    @ExceptionHandler(URIException.class)
    public ResponseEntity<URIException> noSuchLinkException(URIException e) {
        return ResponseEntity.status(e.getErrorCode()).body(e);
    }
}
