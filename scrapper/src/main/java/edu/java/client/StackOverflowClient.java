package edu.java.client;

import edu.java.dto.stack.AnswerDto;
import edu.java.dto.stack.BadgeDto;
import edu.java.dto.stack.GeneralResponse;

public interface StackOverflowClient {
    GeneralResponse<AnswerDto> getAnswersByQuestionId(int id);

    GeneralResponse<BadgeDto> getAllBadges();
}
