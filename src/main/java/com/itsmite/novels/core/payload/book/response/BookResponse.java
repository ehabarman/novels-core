package com.itsmite.novels.core.payload.book.response;

import com.itsmite.novels.core.models.book.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {

    private String id;

    private String title;

    private String description;

    private String coverPhoto;

    private String ownerId;

    private Date createdAt;

    private LocalDateTime updateAt;

    public static BookResponse toResponse(Book book) {
        return book != null ? new BookResponse(
            book.getId(),
            book.getTitle(),
            book.getDescription(),
            book.getCoverPhoto(),
            book.getOwnerId(),
            book.getCreatedAt(),
            book.getUpdatedAt()
        ) : new BookResponse();
    }
}
