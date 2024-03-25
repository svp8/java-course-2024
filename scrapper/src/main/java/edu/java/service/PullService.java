package edu.java.service;

import edu.java.entity.PullEntity;
import java.util.List;

public interface PullService {
    List<PullEntity> getAllByLinkId(int linkId);

    PullEntity add(PullEntity entity);

    void delete(PullEntity entity);
}
