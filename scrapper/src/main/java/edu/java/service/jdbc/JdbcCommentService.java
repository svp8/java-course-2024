package edu.java.service.jdbc;

import edu.java.entity.CommentEntity;
import edu.java.repository.jdbc.JdbcCommentRepository;
import edu.java.service.CommentService;
import java.util.List;

public class JdbcCommentService implements CommentService {
    private final JdbcCommentRepository commentRepository;

    public JdbcCommentService(JdbcCommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<CommentEntity> getAllByLinkId(int linkId) {
        return commentRepository.getAllByLinkId(linkId);
    }

    @Override
    public CommentEntity add(CommentEntity entity) {
        return commentRepository.add(entity);
    }

    @Override
    public void delete(CommentEntity entity) {
        commentRepository.delete(entity);
    }
}
