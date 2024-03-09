package edu.java.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.OffsetDateTime;
@Getter
@AllArgsConstructor
public class ChatEntity {
    private long id;
    private OffsetDateTime createdAt;
}
