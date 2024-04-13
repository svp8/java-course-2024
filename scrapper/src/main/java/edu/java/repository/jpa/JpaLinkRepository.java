package edu.java.repository.jpa;

import edu.java.entity.LinkEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaLinkRepository extends JpaRepository<LinkEntity, Integer> {
    Optional<LinkEntity> findByName(String name);

    @SuppressWarnings("checkstyle:MethodName") List<LinkEntity> findByChats_Id(long chatId);

    @Query(value = "select * from link where EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - last_updated_at))>:seconds ",
           nativeQuery = true)
    List<LinkEntity> findAllLastUpdated(long seconds);
}
