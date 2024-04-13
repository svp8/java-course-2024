package edu.java.repository.jpa;

import edu.java.entity.AnswerEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAnswerRepository extends JpaRepository<AnswerEntity, Long> {
    List<AnswerEntity> findAllByLinkId(Integer linkId);
}
