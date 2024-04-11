package edu.java.repository.jooq;

import edu.java.entity.ChatEntity;
import edu.java.exception.InvalidChatIdException;
import edu.java.repository.ChatRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import static scrapper.domain.jooq.Tables.CHAT;
import static scrapper.domain.jooq.Tables.CHAT_LINK;

@Repository
@Primary
public class JooqChatRepository implements ChatRepository {
    private final DSLContext dsl;

    public JooqChatRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public ChatEntity createChat(long id) {
        try {
            return dsl.insertInto(CHAT, CHAT.ID, CHAT.CREATED_AT).values(
                    id,
                    OffsetDateTime.now()
                )
                .returning(CHAT.fields())
                .fetchOneInto(ChatEntity.class);
        } catch (DuplicateKeyException e) {
            throw new InvalidChatIdException(HttpStatus.BAD_REQUEST.value(), "Chat is already registered");
        }

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
