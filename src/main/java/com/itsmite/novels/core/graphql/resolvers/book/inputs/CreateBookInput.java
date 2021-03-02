package com.itsmite.novels.core.graphql.resolvers.book.inputs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookInput {

    @NotNull
    @NotBlank
    @Length(max = 100)
    private String title;

    @NotNull
    @NotBlank
    @Length(max = 1000)
    private String description;

    private String coverPhoto;

}
