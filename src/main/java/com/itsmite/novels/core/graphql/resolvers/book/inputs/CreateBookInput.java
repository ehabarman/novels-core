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
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookInput {

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
