package edu.java.service.jdbc;

import edu.java.entity.AnswerEntity;
import edu.java.repository.jdbc.JdbcAnswerRepository;
import edu.java.service.AnswerService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class JdbcAnswerService implements AnswerService {
    private final JdbcAnswerRepository answerRepository;

    public JdbcAnswerService(JdbcAnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    public List<AnswerEntity> getAllByLinkId(int linkId) {
        return answerRepository.getAllByLinkId(linkId);
    }

    @Override
    public AnswerEntity add(AnswerEntity entity) {
        return answerRepository.add(entity);
    }

    @Override
    public void delete(AnswerEntity entity) {
        answerRepository.delete(entity);
    }
}
