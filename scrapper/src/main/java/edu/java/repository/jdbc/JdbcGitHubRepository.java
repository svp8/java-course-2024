package edu.java.repository.jdbc;

import edu.java.entity.RepositoryEntity;
import edu.java.repository.github.GitHubRepository;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcGitHubRepository implements GitHubRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcGitHubRepository(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<RepositoryEntity> getRepo(int id) {
        try {
            RepositoryEntity repository = jdbcTemplate.queryForObject(
                "select * from repository where id = ?",
                (resultSet, rowNum) -> {
                    RepositoryEntity repositoryEntity = RepositoryEntity.builder()
                        .id(resultSet.getInt("id"))
                        .branchCount(resultSet.getInt("branch_count"))
                        .pullCount(resultSet.getInt("pull_count"))
                        .build();
                    return repositoryEntity;
                },
                id
            );
            return Optional.of(repository);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public RepositoryEntity update(RepositoryEntity repositoryEntity) {
        jdbcTemplate.update(
            """
                UPDATE repository
                SET branch_count= ?,pull_count=?
                WHERE id = ?;
                """,
            repositoryEntity.getBranchCount(), repositoryEntity.getPullCount(), repositoryEntity.getId()
        );
        return getRepo(repositoryEntity.getId()).get();
    }

    @Override
    public RepositoryEntity add(RepositoryEntity repositoryEntity) {
        jdbcTemplate.update(
            "INSERT INTO repository(id,branch_count,pull_count) values(?,?,?)",
            repositoryEntity.getId(), repositoryEntity.getBranchCount(), repositoryEntity.getPullCount()
        );
        return getRepo(repositoryEntity.getId()).get();
    }
}
