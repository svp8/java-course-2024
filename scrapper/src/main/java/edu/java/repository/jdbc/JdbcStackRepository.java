package edu.java.repository.jdbc;

import edu.java.entity.QuestionEntity;
import edu.java.repository.stack.StackRepository;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcStackRepository implements StackRepository {
    JdbcTemplate jdbcTemplate;

    public JdbcStackRepository(DataSource dataSource) {

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<QuestionEntity> getQuestion(int id) {
        try {
            QuestionEntity question = jdbcTemplate.queryForObject(
                "select * from question where id = ?",
                (resultSet, rowNum) -> {
                    QuestionEntity questionEntity = QuestionEntity.builder()
                        .id(resultSet.getInt("id"))
                        .answerCount(resultSet.getInt("answer_count"))
                        .commentCount(resultSet.getInt("comment_count"))
                        .build();
                    return questionEntity;
                },
                id
            );
            return Optional.of(question);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public QuestionEntity update(QuestionEntity questionEntity) {
        jdbcTemplate.update(
            """
                UPDATE question
                SET answer_count= ?,comment_count=?
                WHERE id = ?;
                """,
            questionEntity.getAnswerCount(), questionEntity.getCommentCount(), questionEntity.getId()
        );
        return getQuestion(questionEntity.getId()).get();
    }

    @Override
    public QuestionEntity add(QuestionEntity questionEntity) {
        jdbcTemplate.update(
            "INSERT INTO question(id,answer_count,comment_count) values(?,?,?)",
            questionEntity.getId(), questionEntity.getAnswerCount(), questionEntity.getCommentCount()
        );
        return getQuestion(questionEntity.getId()).get();
    }
}
