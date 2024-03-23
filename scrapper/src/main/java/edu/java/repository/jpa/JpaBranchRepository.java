package edu.java.repository.jpa;

import edu.java.entity.BranchEntity;
import edu.java.entity.BranchId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBranchRepository extends JpaRepository<BranchEntity, BranchId> {
    List<BranchEntity> findAllByLinkId(Integer linkId);
}
