package com.itsmite.novels.core.graphql.resolvers.user.types;

import com.itsmite.novels.core.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserType {

    private String id;

    private String username;

    private String email;

    private Date createdAt;

    public static UserType fromType(User user) {
        return new UserType(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt());
    }
}
