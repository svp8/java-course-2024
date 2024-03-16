package edu.java.repository.stack;

import edu.java.entity.CommentEntity;
import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<CommentEntity> getById(long id);
    List<CommentEntity> getAllByLinkId(int linkId);

    CommentEntity update(CommentEntity entity);

    CommentEntity add(CommentEntity entity);

    void delete(CommentEntity entity);
}
