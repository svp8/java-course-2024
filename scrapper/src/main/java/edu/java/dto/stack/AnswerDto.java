package edu.java.dto.stack;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.OffsetDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AnswerDto {
    @Getter private int answerId;
    @JsonProperty("is_accepted")
    private boolean isAccepted;
    @Getter private int score;
    @Getter private OffsetDateTime lastActivityDate;

    public AnswerDto(int answerId, boolean isAccepted, int score, OffsetDateTime lastActivityDate) {
        this.answerId = answerId;
        this.isAccepted = isAccepted;
        this.score = score;
        this.lastActivityDate = lastActivityDate;
    }

    public boolean getIsAccepted() {
        return isAccepted;
    }

}