package edu.java.repository.jdbc;

import edu.java.entity.BranchEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import edu.java.repository.github.BranchRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcBranchRepository implements BranchRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcBranchRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static BranchEntity mapper(ResultSet resultSet, int rowNum) throws SQLException {
        BranchEntity branchEntity = BranchEntity.builder()
            .linkId(resultSet.getInt("link_id"))
            .name(resultSet.getString("name"))
            .build();
        return branchEntity;
    }

    @Override
    public Optional<BranchEntity> getByNameAndLinkId(String name,int id) {
        try {
            BranchEntity branch = jdbcTemplate.queryForObject(
                "select * from branch where link_id = ? AND name = ?",
                JdbcBranchRepository::mapper,
                id,name
            );
            return Optional.of(branch);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<BranchEntity> getAllByLinkId(int linkId) {
        try {
            List<BranchEntity> branchEntities = jdbcTemplate.query(
                "select * from branch where link_id = ? ",
                JdbcBranchRepository::mapper,
                linkId
            );
            return branchEntities;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public BranchEntity update(BranchEntity entity) {

        return null;
    }

    @Override
    public BranchEntity add(BranchEntity entity) {
        jdbcTemplate.update(
            "INSERT INTO branch(name,link_id) values(?,?)",
            entity.getName(),entity.getLinkId()
        );
        return getByNameAndLinkId(entity.getName(), entity.getLinkId()).get();
    }

    @Override
    public void delete(BranchEntity entity) {
        jdbcTemplate.update(
            "DELETE FROM branch WHERE link_id = ? and name=?",
            entity.getLinkId(),entity.getName()
        );
    }
}
