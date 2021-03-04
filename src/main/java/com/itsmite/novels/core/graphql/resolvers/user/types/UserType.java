package com.itsmite.novels.core.graphql.resolvers.user.types;

import com.itsmite.novels.core.graphql.resolvers.base.types.ObjectType;
import com.itsmite.novels.core.graphql.resolvers.book.types.BookType;
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
public class UserType implements ObjectType<User> {

    private User user;

    private String id;

    private String username;

    private String email;

    private Date createdAt;

    private List<BookType> writtenBooks;

    public static UserType toType(User user) {
        if (user == null) {
            return null;
        }
        UserType userType = new UserType();
        userType.setEntity(user);
        userType.setId(user.getId());
        userType.setUsername(user.getUsername());
        userType.setEmail(user.getEmail());
        userType.setCreatedAt(user.getCreatedAt());
        userType.setWrittenBooks(new ArrayList<>());
        return userType;
    }

    @Override
    public User getEntity() {
        return this.user;
    }

    @Override
    public void setEntity(User user) {
        this.user = user;
    }
}
