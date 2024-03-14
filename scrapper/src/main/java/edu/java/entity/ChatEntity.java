package edu.java.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import java.time.OffsetDateTime;
@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class ChatEntity {
    private long id;
    private OffsetDateTime createdAt;
}
