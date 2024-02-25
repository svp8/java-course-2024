package edu.java.dto.github;

import java.time.OffsetDateTime;

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
