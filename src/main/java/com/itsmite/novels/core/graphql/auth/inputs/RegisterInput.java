package com.itsmite.novels.core.graphql.auth.inputs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterInput {

    private String username;

    private String password;

    private String email;
}
