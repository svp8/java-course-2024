/*
 * This file is generated by jOOQ.
 */
package scrapper.domain.jooq.tables.pojos;


import jakarta.validation.constraints.Size;

import java.beans.ConstructorProperties;
import java.io.Serializable;

import javax.annotation.processing.Generated;

import org.jetbrains.annotations.NotNull;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Branch implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private Integer linkId;

    public Branch() {}

    public Branch(Branch value) {
        this.name = value.name;
        this.linkId = value.linkId;
    }

    @ConstructorProperties({ "name", "linkId" })
    public Branch(
        @NotNull String name,
        @NotNull Integer linkId
    ) {
        this.name = name;
        this.linkId = linkId;
    }

    /**
     * Getter for <code>BRANCH.NAME</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 1000000000)
    @NotNull
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>BRANCH.NAME</code>.
     */
    public void setName(@NotNull String name) {
        this.name = name;
    }

    /**
     * Getter for <code>BRANCH.LINK_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Integer getLinkId() {
        return this.linkId;
    }

    /**
     * Setter for <code>BRANCH.LINK_ID</code>.
     */
    public void setLinkId(@NotNull Integer linkId) {
        this.linkId = linkId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Branch other = (Branch) obj;
        if (this.name == null) {
            if (other.name != null)
                return false;
        }
        else if (!this.name.equals(other.name))
            return false;
        if (this.linkId == null) {
            if (other.linkId != null)
                return false;
        }
        else if (!this.linkId.equals(other.linkId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.linkId == null) ? 0 : this.linkId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Branch (");

        sb.append(name);
        sb.append(", ").append(linkId);

        sb.append(")");
        return sb.toString();
    }
}
