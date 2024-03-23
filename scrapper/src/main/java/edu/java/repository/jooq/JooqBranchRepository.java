package edu.java.repository.jooq;

import edu.java.entity.BranchEntity;
import edu.java.repository.github.BranchRepository;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import static scrapper.domain.jooq.Tables.BRANCH;

public class JooqBranchRepository implements BranchRepository {
    private final DSLContext dsl;

    public JooqBranchRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Optional<BranchEntity> getByNameAndLinkId(String name, int id) {
        BranchEntity branchEntity = dsl.select()
            .from(BRANCH)
            .where(
                BRANCH.LINK_ID.eq(id)
                    .and(BRANCH.NAME.eq(name))
            ).fetchOneInto(BranchEntity.class);
        if (branchEntity == null) {
            return Optional.empty();
        }
        return Optional.of(branchEntity);
    }

    @Override
    public List<BranchEntity> getAllByLinkId(int linkId) {
        return dsl.select(BRANCH)
            .from(BRANCH)
            .where(BRANCH.LINK_ID.eq(linkId))
            .fetchInto(BranchEntity.class);
    }

    @Override
    public BranchEntity update(BranchEntity entity) {
        return null;
    }

    @Override
    public BranchEntity add(BranchEntity entity) {
        return dsl.insertInto(BRANCH, BRANCH.NAME, BRANCH.LINK_ID)
            .values(
                entity.getName(),
                entity.getLinkId()
            )
            .returning(BRANCH.fields())
            .fetchOneInto(BranchEntity.class);
    }

    @Override
    public void delete(BranchEntity entity) {
        dsl.deleteFrom(BRANCH)
            .where(BRANCH.LINK_ID.eq(entity.getLinkId())
                .and(BRANCH.NAME.eq(entity.getName())))
            .execute();
    }
}
