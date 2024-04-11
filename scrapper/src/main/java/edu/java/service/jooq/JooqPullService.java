package edu.java.service.jooq;

import edu.java.entity.PullEntity;
import edu.java.repository.jooq.JooqPullRepository;
import edu.java.service.PullService;
import java.util.List;

public class JooqPullService implements PullService {
    private final JooqPullRepository pullRepository;

    public JooqPullService(JooqPullRepository pullRepository) {
        this.pullRepository = pullRepository;
    }

    @Override
    public List<PullEntity> getAllByLinkId(int linkId) {
        return pullRepository.getAllByLinkId(linkId);
    }

    @Override
    public PullEntity add(PullEntity entity) {
        return pullRepository.add(entity);
    }

    @Override
    public void delete(PullEntity entity) {
        pullRepository.delete(entity);
    }
}
