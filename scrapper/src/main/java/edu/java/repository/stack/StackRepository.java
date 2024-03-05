package edu.java.repository.stack;

import edu.java.entity.QuestionEntity;
import java.util.Optional;

public interface StackRepository {
    Optional<QuestionEntity> getQuestion(int id);

    QuestionEntity update(QuestionEntity questionEntity);

    QuestionEntity add(QuestionEntity questionEntity);
}
