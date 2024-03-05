package edu.java.mapper;

import edu.java.entity.LinkEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class LinkMapper implements RowMapper<LinkEntity> {
    @Override
    public LinkEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        OffsetDateTime created = OffsetDateTime.ofInstant(
            Instant.ofEpochMilli(resultSet.getTimestamp("created_at").getTime()),
            ZoneId.of("UTC")
        );
        OffsetDateTime lastUpdated = OffsetDateTime.ofInstant(
            Instant.ofEpochMilli(resultSet.getTimestamp("last_updated_at").getTime()),
            ZoneId.of("UTC")
        );
        LinkEntity linkEntity = LinkEntity.builder()
            .id(resultSet.getInt("id"))
            .createdAt(created)
            .name(resultSet.getString("name"))
            .lastUpdatedAt(lastUpdated)
            .build();
        return linkEntity;
    }
}
