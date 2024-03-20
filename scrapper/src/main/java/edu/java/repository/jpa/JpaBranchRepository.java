package edu.java.repository.jpa;

import edu.java.entity.BranchEntity;
import edu.java.entity.BranchId;
import edu.java.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBranchRepository extends JpaRepository<BranchEntity, BranchId> {
}
