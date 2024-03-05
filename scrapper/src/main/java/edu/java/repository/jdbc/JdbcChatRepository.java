package edu.java.repository.jdbc;

import edu.java.entity.ChatEntity;
import edu.java.repository.ChatRepository;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcChatRepository implements ChatRepository {

    JdbcTemplate jdbcTemplate;

    public JdbcChatRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void createChat(long id) {
        jdbcTemplate.update(
            "INSERT INTO chat(id,created_at) values(?,?)",
            id, OffsetDateTime.now()
        );
    }

    @Override
    public Optional<ChatEntity> getChatById(long id) {
        try {
            ChatEntity chat = jdbcTemplate.queryForObject(
                "select * from chat where id = ?",
                (resultSet, rowNum) -> {
                    OffsetDateTime offsetDateTime = OffsetDateTime.ofInstant(
                        Instant.ofEpochMilli(resultSet.getTimestamp("created_at").getTime()),
                        ZoneId.of("UTC")
                    );
                    ChatEntity chatEntity = ChatEntity.builder()
                        .id(resultSet.getLong("id"))
                        .createdAt(offsetDateTime)
                        .build();
                    return chatEntity;
                },
                id
            );
            return Optional.of(chat);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteChat(long id) {
        jdbcTemplate.update(
            "DELETE FROM chat WHERE id = ?",
            id
        );
    }
}
