package edu.java.service.jooq;

import edu.java.entity.AnswerEntity;
import edu.java.repository.jooq.JooqAnswerRepository;
import edu.java.service.AnswerService;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class JooqAnswerService implements AnswerService {
    private final JooqAnswerRepository answerRepository;

    public JooqAnswerService(JooqAnswerRepository answerRepository) {
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
