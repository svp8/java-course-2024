package edu.java.repository.jdbc;

import edu.java.entity.CommentEntity;
import edu.java.repository.stack.CommentRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcCommentRepository implements CommentRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcCommentRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static CommentEntity mapper(ResultSet resultSet, int rowNum) throws SQLException {
        OffsetDateTime time = OffsetDateTime.ofInstant(
            Instant.ofEpochMilli(resultSet.getTimestamp("creation_date").getTime()),
            ZoneId.of("UTC")
        );
        CommentEntity commentEntity = CommentEntity.builder()
            .linkId(resultSet.getInt("link_id"))
            .id(resultSet.getLong("id"))
            .creationDate(time)
            .build();
        return commentEntity;
    }

    @Override
    public Optional<CommentEntity> getById(long id) {
        try {
            CommentEntity comment = jdbcTemplate.queryForObject(
                "select * from comment where id = ?",
                JdbcCommentRepository::mapper,
                id
            );
            return Optional.of(comment);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<CommentEntity> getAllByLinkId(int linkId) {
        try {
            List<CommentEntity> commentEntities = jdbcTemplate.query(
                "select * from comment where link_id = ? ",
                JdbcCommentRepository::mapper,
                linkId
            );
            return commentEntities;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public CommentEntity update(CommentEntity entity) {
        return null;
    }

    @Override
    public CommentEntity add(CommentEntity entity) {
        jdbcTemplate.update(
            "INSERT INTO comment(id,creation_date,link_id) values(?,?,?)",
            entity.getId(), entity.getCreationDate(), entity.getLinkId()
        );
        return getById(entity.getId()).get();
    }

    @Override
    public void delete(CommentEntity entity) {
        jdbcTemplate.update(
            "DELETE FROM comment WHERE id = ?",
            entity.getId()
        );
    }
}
