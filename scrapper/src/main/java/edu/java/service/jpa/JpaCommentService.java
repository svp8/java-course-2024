package edu.java.service.jpa;

import edu.java.entity.CommentEntity;
import edu.java.repository.jpa.JpaCommentRepository;
import edu.java.service.CommentService;
import java.util.List;

public class JpaCommentService implements CommentService {
    private final JpaCommentRepository commentRepository;

    public JpaCommentService(JpaCommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<CommentEntity> getAllByLinkId(int linkId) {
        return commentRepository.findAllByLinkId(linkId);
    }

    @Override
    public CommentEntity add(CommentEntity entity) {
        return commentRepository.save(entity);
    }

    @Override
    public void delete(CommentEntity entity) {
        commentRepository.delete(entity);
    }
}
