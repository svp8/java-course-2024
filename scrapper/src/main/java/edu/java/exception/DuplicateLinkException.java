package edu.java.exception;

public class DuplicateLinkException extends ScrapperException {
    public DuplicateLinkException(int errorCode, String description) {
        super(errorCode, description);
    }
}
