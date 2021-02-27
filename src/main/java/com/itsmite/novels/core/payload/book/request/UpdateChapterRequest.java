package com.itsmite.novels.core.payload.book.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateChapterRequest {

    @NotNull
    @NotEmpty
    @Length(max = 100)
    private String title;

    private String content;

    @Length(max = 1000)
    private String authorNotes;

    @NotNull
    @Builder.Default
    private boolean draft = true;
}
