package edu.java.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
public class BranchId implements Serializable {
    private String name;
    private int linkId;
}
