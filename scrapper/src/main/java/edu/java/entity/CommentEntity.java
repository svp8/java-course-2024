package edu.java.entity;

import java.time.OffsetDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class CommentEntity {
    @Id
    private long id;
    private OffsetDateTime creationDate;
    private int linkId;
}
