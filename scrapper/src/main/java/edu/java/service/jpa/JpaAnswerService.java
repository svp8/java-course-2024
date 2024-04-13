package edu.java.service.jpa;

import edu.java.entity.AnswerEntity;
import edu.java.repository.jpa.JpaAnswerRepository;
import edu.java.service.AnswerService;
import java.util.List;

public class JpaAnswerService implements AnswerService {
    private final JpaAnswerRepository answerRepository;

    public JpaAnswerService(JpaAnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    public List<AnswerEntity> getAllByLinkId(int linkId) {
        return answerRepository.findAllByLinkId(linkId);
    }

    @Override
    public AnswerEntity add(AnswerEntity entity) {
        return answerRepository.save(entity);
    }

    @Override
    public void delete(AnswerEntity entity) {
        answerRepository.deleteById(entity.getId());
    }
}
