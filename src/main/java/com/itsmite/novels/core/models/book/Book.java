package com.itsmite.novels.core.models.book;

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

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@Document
public class Book implements Persistable<String> {

    @Id
    @JsonIgnore
    @Field("_id")
    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    private String id;

    @NotNull
    @NotEmpty
    private String title;

    private String description;

    private String coverPhoto;

    @DBRef
    private Set<Chapter> roles;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Override
    public boolean isNew() {
        return id == null;
    }
}
