package edu.java.repository.jdbc;

import edu.java.entity.ChatEntity;
import edu.java.exception.InvalidChatIdException;
import edu.java.mapper.ChatMapper;
import edu.java.repository.ChatRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcChatRepository implements ChatRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ChatMapper chatMapper;

    public JdbcChatRepository(DataSource dataSource, ChatMapper chatMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.chatMapper = chatMapper;
    }

    @Override
    public ChatEntity createChat(long id) {
        jdbcTemplate.update(
            "INSERT INTO chat(id,created_at) values(?,?)",
            id, OffsetDateTime.now()
        );
        return getChatById(id).get();
    }

    @Override
    public List<ChatEntity> findChatsByLinkId(int id) {
        try {
            List<ChatEntity> chats = jdbcTemplate.query(
                "select * from chat INNER JOIN chat_link cl ON chat.id = cl.chat_id where cl.link_id = ? ",
                chatMapper,
                id
            );
            return chats;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Optional<ChatEntity> getChatById(long id) {
        try {
            ChatEntity chat = jdbcTemplate.queryForObject(
                "select * from chat where id = ?",
                chatMapper,
                id
            );
            return Optional.of(chat);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteChat(long id) {
        int result = jdbcTemplate.update(
            "DELETE FROM chat WHERE id = ?",
            id
        );
        if (result == 0) {
            throw new InvalidChatIdException(HttpStatus.BAD_REQUEST.value(), "Chat is not registered");
        }
    }
}
