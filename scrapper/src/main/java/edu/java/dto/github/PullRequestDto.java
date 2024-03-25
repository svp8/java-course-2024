package edu.java.dto.github;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class PullRequestDto {
    public int id;
    public int number;
    public String url;
    public String state;
    public String title;
    public String body;
    public OffsetDateTime createdAt;
    public OffsetDateTime updatedAt;
}
