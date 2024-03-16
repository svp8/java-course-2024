package edu.java.repository.github;

import edu.java.entity.PullEntity;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import static scrapper.domain.jooq.Tables.PULL_REQUEST;

public class JooqPullRepository implements PullRepository {
    private final DSLContext dsl;

    public JooqPullRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Optional<PullEntity> getById(long id) {
        PullEntity pullEntity = dsl.select(PULL_REQUEST)
            .from(PULL_REQUEST)
            .where(PULL_REQUEST.ID.eq(id)).fetchOneInto(PullEntity.class);
        if (pullEntity == null) {
            return Optional.empty();
        }
        return Optional.of(pullEntity);
    }

    @Override
    public List<PullEntity> getAllByLinkId(int linkId) {
        return dsl.select(PULL_REQUEST)
            .from(PULL_REQUEST)
            .where(PULL_REQUEST.LINK_ID.eq(linkId))
            .fetchInto(PullEntity.class);
    }

    @Override
    public PullEntity update(PullEntity entity) {
        return null;
    }

    @Override
    public PullEntity add(PullEntity entity) {
        return dsl.insertInto(PULL_REQUEST, PULL_REQUEST.ID, PULL_REQUEST.TITLE, PULL_REQUEST.LINK_ID)
            .values(
                entity.getId(),
                entity.getTitle(),
                entity.getLinkId()
            )
            .returning(PULL_REQUEST.fields())
            .fetchOneInto(PullEntity.class);
    }

    @Override
    public void delete(PullEntity entity) {
        dsl.deleteFrom(PULL_REQUEST)
            .where(PULL_REQUEST.ID.eq(entity.getId()))
            .execute();
    }

}
