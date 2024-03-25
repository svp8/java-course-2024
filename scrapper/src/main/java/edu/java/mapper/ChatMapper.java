package edu.java.mapper;

import edu.java.entity.ChatEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class ChatMapper implements RowMapper<ChatEntity> {
    @Override
    public ChatEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        OffsetDateTime created = OffsetDateTime.ofInstant(
            Instant.ofEpochMilli(resultSet.getTimestamp("created_at").getTime()),
            ZoneId.of("UTC")
        );
        ChatEntity chat = ChatEntity.builder()
            .id(resultSet.getLong("id"))
            .createdAt(created)
            .build();
        return chat;
    }
}
