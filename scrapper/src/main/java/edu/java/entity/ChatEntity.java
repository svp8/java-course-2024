package edu.java.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.OffsetDateTime;
@Getter
@Builder
@AllArgsConstructor
public class ChatEntity {
    private long id;
    private OffsetDateTime createdAt;
}
