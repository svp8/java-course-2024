package edu.java.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "comment")
public class CommentEntity {
    @Id
    private long id;
    private OffsetDateTime creationDate;
    private int linkId;
}
