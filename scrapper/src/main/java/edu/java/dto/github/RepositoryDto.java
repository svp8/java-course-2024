package edu.java.dto.github;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RepositoryDto {
    private int id;
    private String name;
    private OffsetDateTime createdAt;
    private String language;
    private int subscribersCount;
}
