package edu.java.repository.jdbc;

import edu.java.entity.LinkEntity;
import edu.java.repository.LinkRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class JDBCLinkRepository implements LinkRepository {
    @Override
    public LinkEntity add(String name, long chatId) {
        return null;
    }

    @Override
    public Optional<LinkEntity> getByLinkName(String name) {
        return Optional.empty();
    }

    @Override
    public List<LinkEntity> findAllByChatId(long chatId) {
        return null;
    }

    @Override
    public List<LinkEntity> findAllLastUpdated() {
        return null;
    }

    @Override
    public LinkEntity remove(int id) {
        return null;
    }
}
