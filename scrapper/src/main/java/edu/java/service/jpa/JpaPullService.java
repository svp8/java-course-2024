package edu.java.service.jpa;

import edu.java.entity.PullEntity;
import edu.java.repository.jpa.JpaPullRepository;
import edu.java.service.PullService;
import java.util.List;

public class JpaPullService implements PullService {
    private final JpaPullRepository pullRepository;

    public JpaPullService(JpaPullRepository pullRepository) {
        this.pullRepository = pullRepository;
    }

    @Override
    public List<PullEntity> getAllByLinkId(int linkId) {
        return pullRepository.findAllByLinkId(linkId);
    }

    @Override
    public PullEntity add(PullEntity entity) {
        return pullRepository.save(entity);
    }

    @Override
    public void delete(PullEntity entity) {
        pullRepository.delete(entity);
    }
}
