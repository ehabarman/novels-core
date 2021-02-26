package com.itsmite.novels.core.payload.book.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookRequest {

    @NotNull
    @NotEmpty
    private String title;

    @NotNull
    @NotEmpty
    private String description;

    private String coverPhoto;
}
