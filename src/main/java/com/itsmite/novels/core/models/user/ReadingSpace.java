package com.itsmite.novels.core.models.user;

import com.itsmite.novels.core.models.book.Book;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents user writings collection in the database.
 *
 * @author Ehab Arman
 */
@Setter
@Getter
@Document
public class ReadingSpace implements Persistable<String> {

    @Id
    @JsonIgnore
    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    private String id;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @DBRef(lazy = true)
    private Set<Book> books;

    public ReadingSpace() {
        this.books = new HashSet<>();
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
