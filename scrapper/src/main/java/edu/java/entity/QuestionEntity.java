package edu.java.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class QuestionEntity {
    private int id;
    private int answerCount;
    private int commentCount;
}
