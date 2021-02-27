package com.itsmite.novels.core.payload.book.response;

import com.itsmite.novels.core.models.book.Chapter;
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
public class ChapterResponse {

    private String id;

    private String title;

    private boolean draft;

    private String content;

    private String authorNotes;

    private Date createdAt;

    private LocalDateTime updateAt;

    public static ChapterResponse toResponse(Chapter chapter) {
        return chapter != null ? new ChapterResponse(
            chapter.getId(),
            chapter.getTitle(),
            chapter.isDraft(),
            chapter.getContent(),
            chapter.getAuthorNotes(),
            chapter.getCreatedAt(),
            chapter.getUpdatedAt()
        ) : null;
    }
}
