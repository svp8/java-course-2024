package edu.java.repository.github;

import edu.java.entity.RepositoryEntity;
import java.util.Optional;

public interface GitHubRepository {
    Optional<RepositoryEntity> getRepo(int id);
    RepositoryEntity update(RepositoryEntity repositoryEntity);
    RepositoryEntity add(RepositoryEntity repositoryEntity);
}
