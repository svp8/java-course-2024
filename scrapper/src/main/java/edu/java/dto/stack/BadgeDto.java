package edu.java.dto.stack;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BadgeDto {
    private String badgeType;
    private int awardCount;
    private String rank;
    private int badgeId;
    private String link;
    private String name;

    public BadgeDto(String badgeType, int awardCount, String rank, int badgeId, String link, String name) {
        this.badgeType = badgeType;
        this.awardCount = awardCount;
        this.rank = rank;
        this.badgeId = badgeId;
        this.link = link;
        this.name = name;
    }
}
