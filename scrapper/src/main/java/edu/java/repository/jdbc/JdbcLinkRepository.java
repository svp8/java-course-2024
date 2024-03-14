package edu.java.repository.jdbc;

import edu.java.entity.LinkEntity;
import edu.java.mapper.LinkMapper;
import edu.java.repository.ChatLinkRepository;
import edu.java.repository.LinkRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private final LinkMapper linkMapper;
    private final ChatLinkRepository chatLinkRepository;

    public JdbcLinkRepository(DataSource dataSource, LinkMapper linkMapper, ChatLinkRepository chatLinkRepository) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.linkMapper = linkMapper;
        this.chatLinkRepository = chatLinkRepository;
    }

    @Override
    public LinkEntity add(String name) {
        jdbcTemplate.update(
            "INSERT INTO link(name,created_at,last_updated_at) values(?,?,?)",
            name, OffsetDateTime.now(), OffsetDateTime.now()
        );
        return getByLinkName(name).get();
    }

    @Override
    public LinkEntity update(LinkEntity link) {
        jdbcTemplate.update(
            """
                UPDATE link
                SET name= ?,created_at=?,last_updated_at=?
                WHERE id = ?;
                """,
            link.getName(), link.getCreatedAt(), link.getLastUpdatedAt(), link.getId()
        );
        return getByLinkName(link.getName()).get();
    }

    @Override
    public Optional<LinkEntity> getByLinkName(String name) {
        try {
            LinkEntity link = jdbcTemplate.queryForObject(
                "select * from link where name = ?",
                linkMapper,
                name
            );
            return Optional.of(link);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<LinkEntity> findAllByChatId(long chatId) {
        List<LinkEntity> links = chatLinkRepository.findLinksByChatId(chatId);
        return links;
    }

    @Override
    public List<LinkEntity> findAllLastUpdated() {
        try {
            List<LinkEntity> links = jdbcTemplate.query(
                "select * from link where EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - last_updated_at))>20 ",
                linkMapper
            );
            return links;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void remove(int id) {
        jdbcTemplate.update(
            "DELETE FROM link WHERE id = ?",
            id
        );
    }
}
