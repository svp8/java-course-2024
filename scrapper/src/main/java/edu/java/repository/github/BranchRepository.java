package edu.java.repository.github;

import edu.java.entity.BranchEntity;
import java.util.List;
import java.util.Optional;

public interface BranchRepository {
    Optional<BranchEntity> getByNameAndLinkId(String name, int id);

    List<BranchEntity> getAllByLinkId(int linkId);

    BranchEntity add(BranchEntity entity);

    void delete(BranchEntity entity);
}
