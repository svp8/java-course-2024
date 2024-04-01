package edu.java.repository.jdbc;

import edu.java.entity.LinkEntity;
import edu.java.exception.NoSuchLinkException;
import edu.java.mapper.LinkMapper;
import edu.java.repository.LinkRepository;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private final LinkMapper linkMapper;
    private final JdbcChatLinkRepository chatLinkRepository;

    public JdbcLinkRepository(DataSource dataSource, LinkMapper linkMapper, JdbcChatLinkRepository chatLinkRepository) {
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
    public List<LinkEntity> findAllLastUpdated(Duration offset) {
        try {
            List<LinkEntity> links = jdbcTemplate.query(
                "select * from link where EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - last_updated_at))>? ",
                linkMapper, offset.toSeconds()
            );
            return links;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void remove(int id) {
        int result = jdbcTemplate.update(
            "DELETE FROM link WHERE id = ?",
            id
        );
        if (result == 0) {
            throw new NoSuchLinkException(HttpStatus.NOT_FOUND.value(), "Link is not registered");
        }
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
}
