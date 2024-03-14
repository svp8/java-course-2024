package edu.java.repository.github;

import edu.java.entity.PullEntity;
import java.util.List;
import java.util.Optional;

public interface PullRepository {
    Optional<PullEntity> getById(int id);
    List<PullEntity> getAllByLinkId(int linkId);
    PullEntity update(PullEntity entity);

    PullEntity add(PullEntity entity);

    void delete(PullEntity entity);
}
