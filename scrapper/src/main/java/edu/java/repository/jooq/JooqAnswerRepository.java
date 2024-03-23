package edu.java.repository.jooq;

import edu.java.entity.AnswerEntity;
import edu.java.repository.stack.AnswerRepository;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import static scrapper.domain.jooq.Tables.ANSWER;

public class JooqAnswerRepository implements AnswerRepository {
    private final DSLContext dsl;

    public JooqAnswerRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Optional<AnswerEntity> getById(long id) {
        AnswerEntity answerEntityEntity = dsl.select(ANSWER)
            .from(ANSWER)
            .where(ANSWER.ID.eq(id))
            .fetchOneInto(AnswerEntity.class);
        if (answerEntityEntity == null) {
            return Optional.empty();
        }
        return Optional.of(answerEntityEntity);
    }

    @Override
    public List<AnswerEntity> getAllByLinkId(int linkId) {
        return dsl.select(ANSWER)
            .from(ANSWER)
            .where(ANSWER.LINK_ID.eq(linkId))
            .fetchInto(AnswerEntity.class);
    }

    @Override
    public AnswerEntity update(AnswerEntity entity) {
        return null;
    }

    @Override
    public AnswerEntity add(AnswerEntity entity) {
        return dsl.insertInto(ANSWER, ANSWER.ID, ANSWER.CREATED_AT, ANSWER.LINK_ID)
            .values(
                entity.getId(),
                entity.getCreationDate(),
                entity.getLinkId()
            )
            .returning(ANSWER.fields())
            .fetchOneInto(AnswerEntity.class);
    }

    @Override
    public void delete(AnswerEntity entity) {
        dsl.deleteFrom(ANSWER)
            .where(ANSWER.ID.eq(entity.getId()))
            .execute();
    }
}
