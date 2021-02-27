package com.itsmite.novels.core.repositories.book;

import com.itsmite.novels.core.models.book.Chapter;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ChapterRepository extends MongoRepository<Chapter, String> {


    Optional<Chapter> findChapterByIdAndDeletedAt(String id, LocalDateTime deletedAt);
}
