package edu.java.repository.github;

import edu.java.entity.RepositoryEntity;

public interface GitHubRepository {
    RepositoryEntity getRepo(int id);
}
