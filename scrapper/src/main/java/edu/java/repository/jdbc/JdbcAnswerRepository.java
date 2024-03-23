package edu.java.repository.jdbc;

import edu.java.entity.AnswerEntity;
import edu.java.repository.stack.AnswerRepository;
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

public class JdbcAnswerRepository implements AnswerRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcAnswerRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static AnswerEntity mapper(ResultSet resultSet, int rowNum) throws SQLException {
        OffsetDateTime time = OffsetDateTime.ofInstant(
            Instant.ofEpochMilli(resultSet.getTimestamp("created_at").getTime()),
            ZoneId.of("UTC")
        );
        AnswerEntity answerEntity = AnswerEntity.builder()
            .linkId(resultSet.getInt("link_id"))
            .id(resultSet.getLong("id"))
            .creationDate(time)
            .build();
        return answerEntity;
    }

    @Override
    public Optional<AnswerEntity> getById(long id) {
        try {
            AnswerEntity answer = jdbcTemplate.queryForObject(
                "select * from answer where id = ?",
                JdbcAnswerRepository::mapper,
                id
            );
            return Optional.of(answer);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<AnswerEntity> getAllByLinkId(int linkId) {
        try {
            List<AnswerEntity> answerEntities = jdbcTemplate.query(
                "select * from answer where link_id = ? ",
                JdbcAnswerRepository::mapper,
                linkId
            );
            return answerEntities;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public AnswerEntity update(AnswerEntity entity) {
        return null;
    }

    @Override
    public AnswerEntity add(AnswerEntity entity) {
        jdbcTemplate.update(
            "INSERT INTO answer(id,created_at,link_id) values(?,?,?)",
            entity.getId(), entity.getCreationDate(), entity.getLinkId()
        );
        return getById(entity.getId()).get();
    }

    @Override
    public void delete(AnswerEntity entity) {
        jdbcTemplate.update(
            "DELETE FROM answer WHERE id = ?",
            entity.getId()
        );
    }
}
