package edu.java.dto.stack;

import lombok.Data;

@Data
public class GeneralResponse<T> {
    private T[] items;
    private boolean hasMore;
}
