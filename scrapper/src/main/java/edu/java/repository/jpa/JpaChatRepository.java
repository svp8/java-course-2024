package edu.java.repository.jpa;

import edu.java.entity.ChatEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRepository extends JpaRepository<ChatEntity, Long> {
    List<ChatEntity> findByLinks_Id(long linkId);
}
