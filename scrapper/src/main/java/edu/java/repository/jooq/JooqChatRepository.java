package edu.java.repository.jooq;

import edu.java.entity.ChatEntity;
import java.time.OffsetDateTime;
import java.util.Optional;
import edu.java.entity.LinkEntity;
import edu.java.repository.ChatRepository;
import org.jooq.DSLContext;
import static scrapper.domain.jooq.Tables.CHAT;
import static scrapper.domain.jooq.Tables.LINK;

public class JooqChatRepository implements ChatRepository {
    private final DSLContext dsl;

    public JooqChatRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public ChatEntity createChat(long id) {
        return dsl.insertInto(CHAT, CHAT.ID, CHAT.CREATED_AT).values(
                id,
                OffsetDateTime.now()
            )
            .returning(CHAT.fields())
            .fetchOneInto(ChatEntity.class);
    }

    @Override
    public Optional<ChatEntity> getChatById(long id) {
        ChatEntity chatEntity = dsl.select(CHAT)
            .from(CHAT)
            .where(CHAT.ID.eq(id)).fetchOneInto(ChatEntity.class);

        if (chatEntity == null) {
            return Optional.empty();
        }
        return Optional.of(chatEntity);
    }

    @Override
    public void deleteChat(long id) {
        dsl.deleteFrom(CHAT)
            .where(CHAT.ID.eq(id))
            .execute();
    }
}
