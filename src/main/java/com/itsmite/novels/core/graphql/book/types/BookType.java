package com.itsmite.novels.core.graphql.book.types;

import com.itsmite.novels.core.models.book.Book;
import com.itsmite.novels.core.models.book.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookType {

    private String id;

    private String title;

    private String description;

    private String coverPhoto;

    private String ownerId;

    private BookStatus status;

    private Date createdAt;

    private LocalDateTime updatedAt;

    public static BookType fromType(Book book) {
        return book != null ? new BookType(
            book.getId(),
            book.getTitle(),
            book.getDescription(),
            book.getCoverPhoto(),
            book.getOwnerId(),
            book.getStatus(),
            book.getCreatedAt(),
            book.getUpdatedAt()
        ) : null;
    }
}
