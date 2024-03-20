package edu.java.repository.jpa;

import edu.java.entity.AnswerEntity;
import edu.java.entity.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAnswerRepository extends JpaRepository<AnswerEntity,Long> {
}
