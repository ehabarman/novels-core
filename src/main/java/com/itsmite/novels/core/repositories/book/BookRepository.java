package com.itsmite.novels.core.repositories.book;

import com.itsmite.novels.core.models.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String> {

    Page<Book> findAll(Pageable pageable);

    Page<Book> findAllByOwnerId(String ownerId, Pageable pageable);
}
