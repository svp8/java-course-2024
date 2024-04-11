package edu.java.repository.jooq;

import edu.java.repository.ChatLinkRepository;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import static scrapper.domain.jooq.Tables.CHAT_LINK;

@Repository
@Primary
public class JooqChatLinkRepository implements ChatLinkRepository {
    private final DSLContext dsl;

    public JooqChatLinkRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public void create(long chatId, int linkId) {
        dsl.insertInto(CHAT_LINK, CHAT_LINK.CHAT_ID, CHAT_LINK.LINK_ID)
            .values(chatId, linkId).execute();
    }

    @Override
    public void remove(long chatId, int linkId) {
        dsl.delete(CHAT_LINK)
            .where(CHAT_LINK.CHAT_ID.eq(chatId)
                .and(CHAT_LINK.LINK_ID.eq(linkId))).execute();
    }
}
