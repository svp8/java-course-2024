package edu.java.repository.jooq;

import edu.java.entity.ChatEntity;
import edu.java.entity.LinkEntity;
import edu.java.repository.ChatLinkRepository;
import java.util.List;
import org.jooq.DSLContext;
import static scrapper.domain.jooq.Tables.CHAT;
import static scrapper.domain.jooq.Tables.CHAT_LINK;
import static scrapper.domain.jooq.Tables.LINK;

public class JooqChatLinkRepository implements ChatLinkRepository {
    private final DSLContext dsl;

    public JooqChatLinkRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public List<LinkEntity> findLinksByChatId(long id) {
        return dsl.select(LINK)
            .from(LINK
                .join(CHAT_LINK)
                .on(LINK.ID.eq(CHAT_LINK.LINK_ID))
            )
            .where(CHAT_LINK.CHAT_ID.eq(id))
            .fetchInto(LinkEntity.class);
    }

    @Override
    public List<ChatEntity> findChatsByLinkId(int id) {

        return dsl.select(CHAT)
            .from(CHAT
                .join(CHAT_LINK)
                .on(CHAT.ID.eq(CHAT_LINK.CHAT_ID))
            )
            .where(CHAT_LINK.LINK_ID.eq(id))
            .fetchInto(ChatEntity.class);
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
