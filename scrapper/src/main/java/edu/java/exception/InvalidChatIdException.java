package edu.java.exception;

public class InvalidChatIdException extends ScrapperException{

    public InvalidChatIdException(int errorCode, String description) {
        super(errorCode, description);
    }
}
