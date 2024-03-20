package edu.java.client;

import edu.java.dto.stack.AnswerDto;
import edu.java.dto.stack.CommentDto;
import edu.java.dto.stack.GeneralResponse;

public interface StackOverflowClient  {
    GeneralResponse<AnswerDto> getAnswersByQuestionId(int id);

    GeneralResponse<CommentDto> getCommentsByQuestionId(int id);
}
