package edu.java.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public abstract class ScrapperException extends RuntimeException {
    private final int errorCode;
    private final String description;
}
