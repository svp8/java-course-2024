package edu.java.service;

import edu.java.entity.CommentEntity;
import java.util.List;

public interface CommentService {
    List<CommentEntity> getAllByLinkId(int linkId);

    CommentEntity add(CommentEntity entity);

    void delete(CommentEntity entity);
}
