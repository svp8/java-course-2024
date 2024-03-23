package edu.java.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chat")
public class ChatEntity {
    @Id
    private long id;
    private OffsetDateTime createdAt;

    public ChatEntity(long id, OffsetDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "chat_link",
               inverseJoinColumns = @JoinColumn(
                   name = "link_id",
                   referencedColumnName = "id"
               ),
               joinColumns = @JoinColumn(
                   name = "chat_id",
                   referencedColumnName = "id"
               ))
    private List<LinkEntity> links = new ArrayList<>();

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChatEntity chat = (ChatEntity) o;
        return id == chat.id && Objects.equals(createdAt, chat.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt);
    }
}
