package com.itsmite.novels.core.graphql.resolvers.book.queries;

import com.itsmite.novels.core.graphql.resolvers.book.types.BookType;
import com.itsmite.novels.core.graphql.resolvers.book.types.ChapterType;
import com.itsmite.novels.core.graphql.resolvers.user.types.UserType;
import com.itsmite.novels.core.models.book.Book;
import com.itsmite.novels.core.models.book.Chapter;
import com.itsmite.novels.core.models.user.User;
import com.itsmite.novels.core.services.user.UserService;
import com.itsmite.novels.core.util.StringUtil;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class BookFieldsQuery implements GraphQLResolver<BookType> {

    private UserService userService;

    @Autowired
    public void autowireBeans(UserService userService) {
        this.userService = userService;
    }

    @SuppressWarnings("Used by graphql reflection")
    public UserType getOwner(BookType book) {
        User user = userService.findById(book.getOwner().getId());
        return UserType.toType(user);
    }

    @SuppressWarnings("Used by graphql reflection")
    public List<ChapterType> getChapters(BookType bookType) {
        return ChapterType.toType(bookType.getEntity().getChapters());
    }

    @SuppressWarnings("Used by graphql reflection")
    public ChapterType getChapter(BookType bookType, String chapterId) {
        Chapter foundChapter = bookType.getEntity().getChapters().stream()
                            .filter(chapter -> Objects.equals(chapter.getId(), chapterId))
                            .findFirst().orElse(null);
        return ChapterType.toType(foundChapter);
    }
}
