package edu.java.entity;

import jakarta.persistence.Column;
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
    @Column(name = "creation_date")
    private OffsetDateTime creationDate;
    @Column(name = "link_id")
    private int linkId;
}
