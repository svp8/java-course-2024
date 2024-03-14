package edu.java.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class RepositoryEntity {
    private int id;
    private int branchCount;
    private int pullCount;
}
