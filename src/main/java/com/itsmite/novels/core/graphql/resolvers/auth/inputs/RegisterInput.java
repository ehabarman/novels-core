package com.itsmite.novels.core.graphql.resolvers.auth.inputs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterInput {

    @NotNull
    @NotEmpty
    @Pattern(regexp = "[\\w]+", message = "Invalid name pattern")
    private String username;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    @Pattern(regexp = ".+@.+[.].+", message = "Invalid email pattern")
    private String email;
}
