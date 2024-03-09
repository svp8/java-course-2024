package edu.java.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.OffsetDateTime;
@Getter
@AllArgsConstructor
public class LinkEntity {
    private int id;
    private String name;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastUpdatedAt;

}
