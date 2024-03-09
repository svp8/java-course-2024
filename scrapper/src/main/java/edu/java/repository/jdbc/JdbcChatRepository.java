package edu.java.repository.jdbc;

import edu.java.repository.ChatRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcChatRepository implements ChatRepository {
    @Override
    public void createChat(long id) {

    }

    @Override
    public void deleteChat(long id) {

    }
}
