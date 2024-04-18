package edu.java.repository.stack;

import edu.java.entity.AnswerEntity;
import java.util.List;
import java.util.Optional;

public interface AnswerRepository {
    Optional<AnswerEntity> getById(long id);

    List<AnswerEntity> getAllByLinkId(int linkId);

    AnswerEntity add(AnswerEntity entity);

    void delete(AnswerEntity entity);
}
