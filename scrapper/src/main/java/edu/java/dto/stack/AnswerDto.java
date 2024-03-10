package edu.java.dto.stack;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.OffsetDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AnswerDto {
    @Getter private final int answerId;
    @JsonProperty("is_accepted")
    private final boolean isAccepted;
    @Getter private final int score;
    @Getter private final OffsetDateTime lastActivityDate;

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
