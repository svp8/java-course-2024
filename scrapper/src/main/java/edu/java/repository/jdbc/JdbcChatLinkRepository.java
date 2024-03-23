package edu.java.repository.jdbc;

import edu.java.entity.ChatEntity;
import edu.java.entity.LinkEntity;
import edu.java.mapper.ChatMapper;
import edu.java.mapper.LinkMapper;
import edu.java.repository.ChatLinkRepository;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcChatLinkRepository implements ChatLinkRepository {
    JdbcTemplate jdbcTemplate;
    private final LinkMapper linkMapper;
    private final ChatMapper chatMapper;

    public JdbcChatLinkRepository(DataSource dataSource, LinkMapper linkMapper, ChatMapper chatMapper) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.linkMapper = linkMapper;
        this.chatMapper = chatMapper;
    }

    @Override
    public List<LinkEntity> findLinksByChatId(long id) {
        try {
            List<LinkEntity> links = jdbcTemplate.query(
                "select * from link INNER JOIN chat_link cl ON link.id = cl.link_id where cl.chat_id = ? ",
                linkMapper,
                id
            );
            return links;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<ChatEntity> findChatsByLinkId(int id) {
        try {
            List<ChatEntity> chats = jdbcTemplate.query(
                "select * from chat INNER JOIN chat_link cl ON chat.id = cl.chat_id where cl.link_id = ? ",
                chatMapper,
                id
            );
            return chats;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
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
