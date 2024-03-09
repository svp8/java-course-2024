package edu.java.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RepositoryEntity {
    private int id;
    private int branchCount;
    private int pullCount;
}
