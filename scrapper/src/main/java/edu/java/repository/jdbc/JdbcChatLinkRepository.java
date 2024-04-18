package edu.java.repository.jdbc;

import edu.java.mapper.ChatMapper;
import edu.java.mapper.LinkMapper;
import edu.java.repository.ChatLinkRepository;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcChatLinkRepository implements ChatLinkRepository {
    JdbcTemplate jdbcTemplate;

    public JdbcChatLinkRepository(DataSource dataSource, LinkMapper linkMapper, ChatMapper chatMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void create(long chatId, int linkId) {
        jdbcTemplate.update(
            "INSERT INTO chat_link(chat_id,link_id) values(?,?)",
            chatId, linkId
        );
    }

    @Override
    public void remove(long chatId, int linkId) {
        jdbcTemplate.update(
            "DELETE FROM chat_link WHERE chat_id = ? AND link_id = ?",
            chatId, linkId
        );
    }
}
