package edu.java.service;

import edu.java.entity.BranchEntity;
import java.util.List;

public interface BranchService {

    List<BranchEntity> getAllByLinkId(int linkId);

    BranchEntity add(BranchEntity entity);

    void delete(BranchEntity entity);
}
