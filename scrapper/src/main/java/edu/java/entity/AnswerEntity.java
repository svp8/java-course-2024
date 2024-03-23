package edu.java.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "answer")
public class AnswerEntity {
    @Id
    private long id;
    @Column(name = "created_at")
    private OffsetDateTime creationDate;
    private int linkId;

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AnswerEntity that = (AnswerEntity) o;
        return id == that.id && linkId == that.linkId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, linkId);
    }
}
