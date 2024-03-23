package edu.java.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Entity
@Table(name = "link")
public class LinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastUpdatedAt;

    public LinkEntity(int id, String name, OffsetDateTime createdAt, OffsetDateTime lastUpdatedAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "chat_link",
               joinColumns = @JoinColumn(
                   name = "link_id",
                   referencedColumnName = "id"
               ),
               inverseJoinColumns = @JoinColumn(
                   name = "chat_id",
                   referencedColumnName = "id"
               ))
    private List<ChatEntity> chats = new ArrayList<>();

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LinkEntity that = (LinkEntity) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

}
