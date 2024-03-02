package edu.java.dto.stack;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BadgeDto {
    private final String badgeType;
    private final int awardCount;
    private final String rank;
    private final int badgeId;
    private final String link;
    private final String name;

    public BadgeDto(String badgeType, int awardCount, String rank, int badgeId, String link, String name) {
        this.badgeType = badgeType;
        this.awardCount = awardCount;
        this.rank = rank;
        this.badgeId = badgeId;
        this.link = link;
        this.name = name;
    }
}
