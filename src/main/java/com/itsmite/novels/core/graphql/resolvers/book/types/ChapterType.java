package com.itsmite.novels.core.graphql.resolvers.book.types;

import com.itsmite.novels.core.graphql.resolvers.base.types.ObjectType;
import com.itsmite.novels.core.models.book.Chapter;
import com.itsmite.novels.core.util.CollectionUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChapterType implements ObjectType<Chapter> {

    private Chapter chapter;

    private String id;

    private String title;

    private boolean draft;

    private String content;

    private String authorNotes;

    private Date createdAt;

    private LocalDateTime updateAt;

    public static ChapterType toType(Chapter chapter) {
        if (chapter == null) {
            return null;
        }
        ChapterType chapterType = new ChapterType();
        chapterType.setEntity(chapter);
        chapterType.setId(chapter.getId());
        chapterType.setTitle(chapter.getTitle());
        chapterType.setDraft(chapter.isDraft());
        chapterType.setContent(chapter.getContent());
        chapterType.setAuthorNotes(chapter.getAuthorNotes());
        chapterType.setCreatedAt(chapter.getCreatedAt());
        chapterType.setUpdateAt(chapter.getUpdatedAt());
        return chapterType;
    }

    public static List<ChapterType> toType(Collection<Chapter> chapters) {
        if (CollectionUtil.isNullOrEmpty(chapters)) {
            return Collections.emptyList();
        }
        return chapters.stream()
                    .map(ChapterType::toType)
                    .collect(Collectors.toList());
    }

    @Override
    public Chapter getEntity() {
        return chapter;
    }

    @Override
    public void setEntity(Chapter entity) {
        this.chapter = entity;
    }
}
