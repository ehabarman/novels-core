package com.itsmite.novels.core.graphql.resolvers.book.inputs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateChapterInput {

    @NotNull
    @NotEmpty
    private String bookId;

    @NotNull
    @NotEmpty
    private String chapterId;

    @NotNull
    @NotEmpty
    @Length(max = 100)
    private String title;

    private String content;

    @Length(max = 1000)
    private String authorNotes;

    @NotNull
    private boolean draft;

}
