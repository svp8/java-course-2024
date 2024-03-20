package edu.java.repository.jpa;

import edu.java.entity.ChatEntity;
import edu.java.entity.PullEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPullRepository extends JpaRepository<PullEntity,Long> {
}
