package edu.java.repository.jpa;

import edu.java.entity.ChatEntity;
import edu.java.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCommentRepository extends JpaRepository<CommentEntity,Integer> {
}
