package com.itsmite.novels.core.graphql.resolvers.book.inputs;

import com.itsmite.novels.core.models.book.BookStatus;
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
public class UpdateBookInput {

    @NotNull
    @NotEmpty
    private String bookId;

    @NotNull
    @NotEmpty
    @Length(max = 100)
    private String title;

    @NotNull
    @NotEmpty
    @Length(max = 1000)
    private String description;

    @NotNull
    private BookStatus status;

    private String coverPhoto;
}
