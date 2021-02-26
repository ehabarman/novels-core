package com.itsmite.novels.core.services.book;

import com.itsmite.novels.core.errors.exceptions.ResourceNotFoundException;
import com.itsmite.novels.core.errors.exceptions.UnauthorizedResourceAction;
import com.itsmite.novels.core.models.book.Book;
import com.itsmite.novels.core.models.security.ERole;
import com.itsmite.novels.core.models.user.WritingSpace;
import com.itsmite.novels.core.repositories.book.BookRepository;
import com.itsmite.novels.core.repositories.user.WritingSpaceRepository;
import com.itsmite.novels.core.util.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Service
public class BookService {

    private BookRepository         bookRepository;
    private WritingSpaceRepository writingSpaceRepository;

    @Autowired
    public void autowireBeans(BookRepository bookRepository, WritingSpaceRepository writingSpaceRepository) {
        this.bookRepository = bookRepository;
        this.writingSpaceRepository = writingSpaceRepository;
    }

    public Book getEditableBook(String bookId, String userId, Set<ERole> roles) {
        Book book = bookRepository.findBookByIdAndDeletedAt(bookId, null)
                                  .orElseThrow(() -> new ResourceNotFoundException("Book of id " + bookId));
        if (!book.getOwnerId().equals(userId)) {
            if (CollectionUtil.isNullOrEmpty(roles) || !roles.contains(ERole.ADMIN)) {
                throw new UnauthorizedResourceAction("Book", bookId);
            }
        }
        return book;
    }

    public Book createBook(String title, String description, String coverPhoto, String ownerId, WritingSpace writingSpace) {
        Book book = new Book();
        book.setTitle(title);
        book.setDescription(description);
        book.setCoverPhoto(coverPhoto);
        book.setOwnerId(ownerId);
        book = bookRepository.save(book);
        writingSpace.getBooks().add(book);
        writingSpaceRepository.save(writingSpace);
        return book;
    }

    public Book deleteBook(Book book, WritingSpace writingSpace) {
        book.setDeletedAt(LocalDateTime.now());
        book = bookRepository.save(book);
        writingSpace.getBooks().remove(book);
        writingSpaceRepository.save(writingSpace);
        return book;
    }
}
