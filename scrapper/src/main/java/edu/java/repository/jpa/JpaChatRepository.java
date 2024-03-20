package edu.java.repository.jpa;

import edu.java.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRepository extends JpaRepository<ChatEntity,Long> {
}
