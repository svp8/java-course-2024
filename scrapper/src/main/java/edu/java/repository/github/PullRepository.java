package edu.java.repository.github;

import edu.java.entity.PullEntity;
import java.util.List;
import java.util.Optional;

public interface PullRepository {
    Optional<PullEntity> getById(long id);

    List<PullEntity> getAllByLinkId(int linkId);

    PullEntity add(PullEntity entity);

    void delete(PullEntity entity);
}
