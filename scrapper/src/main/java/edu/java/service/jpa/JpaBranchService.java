package edu.java.service.jpa;

import edu.java.entity.BranchEntity;
import edu.java.repository.jpa.JpaBranchRepository;
import edu.java.service.BranchService;
import java.util.List;

public class JpaBranchService implements BranchService {
    private final JpaBranchRepository branchRepository;

    public JpaBranchService(JpaBranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    @Override
    public List<BranchEntity> getAllByLinkId(int linkId) {
        return branchRepository.findAllByLinkId(linkId);
    }

    @Override
    public BranchEntity add(BranchEntity entity) {
        return branchRepository.save(entity);
    }

    @Override
    public void delete(BranchEntity entity) {
        branchRepository.delete(entity);
    }
}
