package edu.java.repository.jpa;

import edu.java.entity.PullEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPullRepository extends JpaRepository<PullEntity, Long> {
    List<PullEntity> findAllByLinkId(Integer linkId);
}
