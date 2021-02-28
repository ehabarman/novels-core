package com.itsmite.novels.core.graphql.resolvers.book.types;

import com.itsmite.novels.core.models.book.Book;
import com.itsmite.novels.core.models.book.BookStatus;
import com.itsmite.novels.core.util.CollectionUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    public static List<BookType> fromType(Collection<Book> books) {
        if (CollectionUtil.isNullOrEmpty(books)) {
            return Collections.emptyList();
        }
        return books.stream()
                    .map(BookType::fromType)
                    .collect(Collectors.toList());
    }
}
