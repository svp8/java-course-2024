package edu.java.service.jooq;

import edu.java.entity.BranchEntity;
import edu.java.repository.jooq.JooqBranchRepository;
import edu.java.service.BranchService;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class JooqBranchService implements BranchService {
    private final JooqBranchRepository branchRepository;

    public JooqBranchService(JooqBranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @Override
    public List<BranchEntity> getAllByLinkId(int linkId) {
        return branchRepository.getAllByLinkId(linkId);
    }

    @Override
    public BranchEntity add(BranchEntity entity) {
        return branchRepository.add(entity);
    }

    @Override
    public void delete(BranchEntity entity) {
        branchRepository.delete(entity);
    }
}
