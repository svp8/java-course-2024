package edu.java.service.jdbc;

import edu.java.entity.PullEntity;
import edu.java.repository.jdbc.JdbcPullRepository;
import edu.java.service.PullService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class JdbcPullService implements PullService {
    private final JdbcPullRepository pullRepository;

    public JdbcPullService(JdbcPullRepository pullRepository) {
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
