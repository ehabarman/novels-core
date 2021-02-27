package com.itsmite.novels.core.models.book;

import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Setter
@Getter
@Document
public class Chapter implements Persistable<String> {

    @Id
    @JsonIgnore
    @NotNull
    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    private String id;

    @NotNull
    @NotEmpty
    private String title;

    // TODO: content should be in a separated document to support lazy loading
    private String content;

    private String authorNotes;

    private String bookId;

    private boolean draft;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Override
    public boolean isNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (this.id == null) {
            return false;
        }

        if (!(object instanceof Chapter)) {
            return false;
        }

        Chapter chapter = (Chapter)object;
        return Objects.equals(this.getId(), chapter.getId());
    }

    @Override
    public int hashCode() {
        return this.id != null ? id.hashCode() : this.toString().hashCode();
    }
}