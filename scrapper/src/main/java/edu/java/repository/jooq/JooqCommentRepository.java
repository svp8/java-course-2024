package edu.java.repository.jooq;

import edu.java.entity.CommentEntity;
import edu.java.repository.stack.CommentRepository;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import static scrapper.domain.jooq.Tables.COMMENT;

public class JooqCommentRepository implements CommentRepository {
    private final DSLContext dsl;

    public JooqCommentRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Optional<CommentEntity> getById(long id) {
        CommentEntity answerEntityEntity = dsl.select(COMMENT)
            .from(COMMENT)
            .where(COMMENT.ID.eq(id))
            .fetchOneInto(CommentEntity.class);
        if (answerEntityEntity == null) {
            return Optional.empty();
        }
        return Optional.of(answerEntityEntity);
    }

    @Override
    public List<CommentEntity> getAllByLinkId(int linkId) {
        return dsl.select(COMMENT)
            .from(COMMENT)
            .where(COMMENT.LINK_ID.eq(linkId))
            .fetchInto(CommentEntity.class);
    }

    @Override
    public CommentEntity update(CommentEntity entity) {
        return null;
    }

    @Override
    public CommentEntity add(CommentEntity entity) {
        return dsl.insertInto(COMMENT, COMMENT.ID, COMMENT.CREATION_DATE, COMMENT.LINK_ID)
            .values(
                entity.getId(),
                entity.getCreationDate(),
                entity.getLinkId()
            )
            .returning(COMMENT.fields())
            .fetchOneInto(CommentEntity.class);
    }

    @Override
    public void delete(CommentEntity entity) {
        dsl.deleteFrom(COMMENT)
            .where(COMMENT.ID.eq(entity.getId()))
            .execute();
    }
}
