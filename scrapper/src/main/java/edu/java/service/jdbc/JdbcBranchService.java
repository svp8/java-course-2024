package edu.java.service.jdbc;

import edu.java.entity.BranchEntity;
import edu.java.repository.jdbc.JdbcBranchRepository;
import edu.java.service.BranchService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class JdbcBranchService implements BranchService {
    private final JdbcBranchRepository branchRepository;

    public JdbcBranchService(JdbcBranchRepository branchRepository) {
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
