package com.itsmite.novels.core.payload.book.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookRequest {

    @NotNull
    @NotEmpty
    @Length(max = 100)
    private String title;

    @NotNull
    @NotEmpty
    @Length(max = 1000)
    private String description;

    private String coverPhoto;
}
