package com.itsmite.novels.core.repositories.book;

import com.itsmite.novels.core.models.book.Chapter;
import com.itsmite.novels.core.repositories.ResourceRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ChapterRepository extends ResourceRepository<Chapter, String> {

    Optional<Chapter> findChapterByIdAndDeletedAt(String id, LocalDateTime deletedAt);
}
