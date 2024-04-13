package edu.java.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class BranchId implements Serializable {
    private String name;
    private int linkId;
}
