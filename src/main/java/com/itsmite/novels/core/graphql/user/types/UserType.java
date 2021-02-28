package com.itsmite.novels.core.graphql.user.types;

import com.itsmite.novels.core.graphql.book.types.BookType;
import com.itsmite.novels.core.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private List<BookType> writtenBooks;

    public static UserType fromType(User user) {
        return user != null
               ? new UserType(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt(), new ArrayList<>())
               : null;
    }
}
