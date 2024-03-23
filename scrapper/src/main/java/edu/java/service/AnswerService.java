package edu.java.service;

import edu.java.entity.AnswerEntity;
import java.util.List;

public interface AnswerService {
    List<AnswerEntity> getAllByLinkId(int linkId);

    AnswerEntity add(AnswerEntity entity);

    void delete(AnswerEntity entity);
}
