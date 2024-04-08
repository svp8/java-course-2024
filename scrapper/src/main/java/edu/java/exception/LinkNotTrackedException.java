package edu.java.exception;

public class LinkNotTrackedException extends ScrapperException {
    public LinkNotTrackedException(int errorCode, String description) {
        super(errorCode, description);
    }
}
