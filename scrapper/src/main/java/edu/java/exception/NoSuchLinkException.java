package edu.java.exception;

public class NoSuchLinkException extends ScrapperException {

    public NoSuchLinkException(int errorCode, String description) {
        super(errorCode, description);
    }
}
