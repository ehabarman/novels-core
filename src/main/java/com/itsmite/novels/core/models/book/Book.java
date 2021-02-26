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
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Document
public class Book implements Persistable<String> {

    @Id
    @JsonIgnore
    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    private String id;

    @NotNull
    @NotEmpty
    private String title;

    private String description;

    private String coverPhoto;

    private String ownerId;

    @DBRef(lazy = true)
    private Set<Chapter> chapters;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public Book() {
        this.chapters = new HashSet<>();
    }

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

        if (!(object instanceof Book)) {
            return false;
        }

        Book book = (Book)object;
        return this.id.equals(book.getId());
    }
}
