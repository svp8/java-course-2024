package edu.java.repository.jdbc;

import edu.java.entity.BranchEntity;
import edu.java.entity.PullEntity;
import edu.java.repository.github.PullRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcPullRepository implements PullRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcPullRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static PullEntity mapper(ResultSet resultSet, int rowNum) throws SQLException {
        PullEntity pullEntity = PullEntity.builder()
            .linkId(resultSet.getInt("link_id"))
            .id(resultSet.getInt("id"))
            .title(resultSet.getString("title"))
            .build();
        return pullEntity;
    }

    @Override
    public Optional<PullEntity> getById(long id) {
        try {
            PullEntity pull = jdbcTemplate.queryForObject(
                "select * from pull_request where id = ? ",
                JdbcPullRepository::mapper,
                id
            );
            return Optional.of(pull);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<PullEntity> getAllByLinkId(int linkId) {
        try {
            List<PullEntity> pullEntities = jdbcTemplate.query(
                "select * from pull_request where link_id = ? ",
                JdbcPullRepository::mapper,
                linkId
            );
            return pullEntities;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public PullEntity update(PullEntity entity) {
        return null;
    }

    @Override
    public PullEntity add(PullEntity entity) {
        jdbcTemplate.update(
            "INSERT INTO pull_request(id,title,link_id) values(?,?,?)",
            entity.getId(),entity.getTitle(),entity.getLinkId()
        );
        return getById(entity.getId()).get();
    }

    @Override
    public void delete(PullEntity entity) {
        jdbcTemplate.update(
            "DELETE FROM pull_request WHERE id = ?",
            entity.getId()
        );
    }
}
