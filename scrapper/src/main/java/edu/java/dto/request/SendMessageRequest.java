package edu.java.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SendMessageRequest {
    private long chatId;
    private String message;
}
