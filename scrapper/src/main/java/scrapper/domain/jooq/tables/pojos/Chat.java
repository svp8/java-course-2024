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
public class Chat implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private OffsetDateTime createdAt;

    public Chat() {
    }

    public Chat(Chat value) {
        this.id = value.id;
        this.createdAt = value.createdAt;
    }

    @ConstructorProperties({"id", "createdAt"})
    public Chat(
        @NotNull Long id,
        @Nullable OffsetDateTime createdAt
    ) {
        this.id = id;
        this.createdAt = createdAt;
    }

    /**
     * Getter for <code>CHAT.ID</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public Long getId() {
        return this.id;
    }

    /**
     * Setter for <code>CHAT.ID</code>.
     */
    public void setId(@NotNull Long id) {
        this.id = id;
    }

    /**
     * Getter for <code>CHAT.CREATED_AT</code>.
     */
    @Nullable
    public OffsetDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>CHAT.CREATED_AT</code>.
     */
    public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
        this.createdAt = createdAt;
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
        final Chat other = (Chat) obj;
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
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.createdAt == null) ? 0 : this.createdAt.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Chat (");

        sb.append(id);
        sb.append(", ").append(createdAt);

        sb.append(")");
        return sb.toString();
    }
}
