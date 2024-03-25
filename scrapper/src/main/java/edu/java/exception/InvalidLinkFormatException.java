package edu.java.exception;

public class InvalidLinkFormatException extends ScrapperException {
    public InvalidLinkFormatException(int errorCode, String description) {
        super(errorCode, description);
    }
}
