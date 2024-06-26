/*
 * This file is generated by jOOQ.
 */

package scrapper.domain.jooq.tables.pojos;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
@SuppressWarnings({"all", "unchecked", "rawtypes", "this-escape"})
public class Answer implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private OffsetDateTime createdAt;
    private Integer linkId;

    public Answer() {
    }

    public Answer(Answer value) {
        this.id = value.id;
        this.createdAt = value.createdAt;
        this.linkId = value.linkId;
    }

    @ConstructorProperties({"id", "createdAt", "linkId"})
    public Answer(
        @NotNull Long id,
        @Nullable OffsetDateTime createdAt,
        @NotNull Integer linkId
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.linkId = linkId;
    }

    /**
     * Getter for <code>ANSWER.ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>ANSWER.ID</code>.
     */
    public void setId(@NotNull Long id) {
        this.id = id;
    }

    /**
     * Getter for <code>ANSWER.CREATED_AT</code>.
     */
    @Nullable
    public OffsetDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>ANSWER.CREATED_AT</code>.
     */
    public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Getter for <code>ANSWER.LINK_ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Integer getLinkId() {
        return this.linkId;
    }

    /**
     * Setter for <code>ANSWER.LINK_ID</code>.
     */
    public void setLinkId(@NotNull Integer linkId) {
        this.linkId = linkId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Answer other = (Answer) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        if (this.createdAt == null) {
            if (other.createdAt != null) {
                return false;
            }
        } else if (!this.createdAt.equals(other.createdAt)) {
            return false;
        }
        if (this.linkId == null) {
            if (other.linkId != null) {
                return false;
            }
        } else if (!this.linkId.equals(other.linkId)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.createdAt == null) ? 0 : this.createdAt.hashCode());
        result = prime * result + ((this.linkId == null) ? 0 : this.linkId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Answer (");

        sb.append(id);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(linkId);

        sb.append(")");
        return sb.toString();
    }
}
