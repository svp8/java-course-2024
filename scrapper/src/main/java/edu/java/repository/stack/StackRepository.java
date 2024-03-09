package edu.java.repository.stack;

import edu.java.entity.QuestionEntity;

public interface StackRepository {
    QuestionEntity getQuestion(int id);
}
