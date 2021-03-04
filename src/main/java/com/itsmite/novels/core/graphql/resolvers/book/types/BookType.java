package com.itsmite.novels.core.graphql.resolvers.book.types;

import com.itsmite.novels.core.graphql.resolvers.base.types.ObjectType;
import com.itsmite.novels.core.graphql.resolvers.user.types.UserType;
import com.itsmite.novels.core.models.book.Book;
import com.itsmite.novels.core.models.book.BookStatus;
import com.itsmite.novels.core.util.CollectionUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class BookType implements ObjectType<Book> {

    private Book book;

    private String id;

    private String title;

    private String description;

    private String coverPhoto;

    private UserType owner;

    private BookStatus status;

    private ChapterType chapter;

    private List<ChapterType> chapters;

    private Date createdAt;

    private LocalDateTime updatedAt;

    public static BookType toType(Book book) {
        if (book == null) {
            return null;
        }
        BookType bookType = new BookType();
        bookType.setEntity(book);
        bookType.setId(book.getId());
        bookType.setTitle(book.getTitle());
        bookType.setDescription(book.getDescription());
        bookType.setCoverPhoto(book.getCoverPhoto());
        bookType.setStatus(book.getStatus());
        bookType.setChapter(null);
        bookType.setChapters(new ArrayList<>());
        bookType.setCreatedAt(book.getCreatedAt());
        bookType.setUpdatedAt(book.getUpdatedAt());

        UserType user = new UserType();
        user.setId(book.getOwnerId());
        bookType.setOwner(user);

        return bookType;
    }

    public static List<BookType> toType(Collection<Book> books) {
        if (CollectionUtil.isNullOrEmpty(books)) {
            return Collections.emptyList();
        }
        return books.stream()
                    .map(BookType::toType)
                    .collect(Collectors.toList());
    }

    @Override
    public Book getEntity() {
        return book;
    }

    @Override
    public void setEntity(Book entity) {
        this.book = entity;
    }
}
