package com.itsmite.novels.core.models.user;

import com.itsmite.novels.core.models.security.Role;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

/**
 * Represents user table in the database.
 *
 * @author Ehab Arman
 */
@Setter
@Getter
@Document(collection = "user")
public class User implements Persistable<String> {

    @Id
    @JsonIgnore
    @Field("_id")
    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    private String id;

    private String email;

    private String password;

    private String username;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private Date deletedAt;

    private boolean enabled;

    private boolean verified;

    @DBRef
    private Set<Role> roles;

    @DBRef
    private ReadingSpace readingSpace = new ReadingSpace();

    @DBRef
    private WritingSpace writingSpace = new WritingSpace();

    @Override
    public boolean isNew() {
        return id == null;
    }
}
