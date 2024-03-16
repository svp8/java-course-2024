package edu.java.repository.jooq;

import edu.java.entity.LinkEntity;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import edu.java.repository.LinkRepository;
import org.jooq.DSLContext;
import org.jooq.DatePart;
import static org.jooq.impl.DSL.currentTimestamp;
import static org.jooq.impl.DSL.extract;
import static scrapper.domain.jooq.Tables.LINK;

public class JooqLinkRepository implements LinkRepository {
    private final JooqChatLinkRepository chatLinkRepository;
    private final DSLContext dsl;

    public JooqLinkRepository(JooqChatLinkRepository chatLinkRepository, DSLContext dsl) {
        this.chatLinkRepository = chatLinkRepository;
        this.dsl = dsl;
    }

    @Override
    public LinkEntity add(String name) {
        return dsl.insertInto(LINK, LINK.NAME, LINK.CREATED_AT, LINK.LAST_UPDATED_AT)
            .values(name,
                OffsetDateTime.now(),
                OffsetDateTime.now()
            )
            .returning(LINK.fields())
            .fetchOneInto(LinkEntity.class);
    }

    @Override
    public LinkEntity update(LinkEntity link) {

        return dsl.update(LINK)
            .set(LINK.LAST_UPDATED_AT, link.getLastUpdatedAt())
            .where(LINK.ID.eq(link.getId()))
            .returning(LINK.fields())
            .fetchOneInto(LinkEntity.class);
    }

    @Override
    public Optional<LinkEntity> getByLinkName(String name) {
        LinkEntity linkEntity = dsl.select(LINK)
            .from(LINK)
            .where(LINK.NAME.eq(name)).fetchOneInto(LinkEntity.class);
        if (linkEntity == null) {
            return Optional.empty();
        }
        return Optional.of(linkEntity);
    }

    @Override
    public List<LinkEntity> findAllByChatId(long chatId) {
        List<LinkEntity> links = chatLinkRepository.findLinksByChatId(chatId);
        return links;
    }

    @Override
    public List<LinkEntity> findAllLastUpdated(Duration offset) {
        return dsl.select(LINK)
            .from(LINK)
            .where(extract(currentTimestamp()
                .sub(LINK.LAST_UPDATED_AT), DatePart.EPOCH)
                .gt((int) offset.toSeconds()))
            .fetchInto(LinkEntity.class);
    }

    @Override
    public void remove(int id) {
        dsl.deleteFrom(LINK)
            .where(LINK.ID.eq(id))
            .execute();
    }
}
