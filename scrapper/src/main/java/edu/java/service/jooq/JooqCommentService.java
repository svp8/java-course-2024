package edu.java.service.jooq;

import edu.java.entity.CommentEntity;
import edu.java.repository.jooq.JooqCommentRepository;
import edu.java.service.CommentService;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class JooqCommentService implements CommentService {
    private final JooqCommentRepository commentRepository;

    public JooqCommentService(JooqCommentRepository commentRepository) {
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
