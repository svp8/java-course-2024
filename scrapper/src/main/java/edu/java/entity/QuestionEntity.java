package edu.java.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class QuestionEntity {
    private int id;
    private int answerCount;
    private int commentCount;
}
