package com.itsmite.novels.core.repositories.book;

import com.itsmite.novels.core.models.book.Book;
import com.itsmite.novels.core.repositories.ResourceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepository extends ResourceRepository<Book, String> {

    Page<Book> findAll(Pageable pageable);

    Page<Book> findAllByOwnerId(String ownerId, Pageable pageable);
}
