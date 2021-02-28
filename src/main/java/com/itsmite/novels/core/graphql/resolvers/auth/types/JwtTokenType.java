package com.itsmite.novels.core.graphql.resolvers.auth.types;

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
public class JwtTokenType {

    private String token;

    private String expirationDate;
}
