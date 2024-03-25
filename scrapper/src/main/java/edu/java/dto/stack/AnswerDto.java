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
    @Getter private long answerId;
    @JsonProperty("is_accepted")
    private boolean isAccepted;

    @Getter private OffsetDateTime creationDate;
    @Getter private int score;
    @Getter private OffsetDateTime lastActivityDate;

    public AnswerDto(
        int answerId,
        boolean isAccepted,
        OffsetDateTime creationDate,
        int score,
        OffsetDateTime lastActivityDate
    ) {
        this.answerId = answerId;
        this.isAccepted = isAccepted;
        this.creationDate = creationDate;
        this.score = score;
        this.lastActivityDate = lastActivityDate;
    }

    public boolean getIsAccepted() {
        return isAccepted;
    }

}
