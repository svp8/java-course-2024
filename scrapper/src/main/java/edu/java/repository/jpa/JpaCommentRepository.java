package edu.java.repository.jpa;

import edu.java.entity.CommentEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByLinkId(long linkId);
}
