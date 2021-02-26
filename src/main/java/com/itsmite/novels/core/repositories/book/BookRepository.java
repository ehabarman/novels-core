package com.itsmite.novels.core.repositories.book;

import com.itsmite.novels.core.models.book.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {

    Optional<Book> findBookByIdAndDeletedAt(String id, LocalDateTime deletedAt);

}
