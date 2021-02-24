package com.itsmite.novels.core.models.user;

import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * Represents user writings collection in the database.
 *
 * @author Ehab Arman
 */
@Setter
@Getter
@Document(collection = "writing_space")
public class WritingSpace implements Persistable<String> {

    @Id
    @JsonIgnore
    @Field("_id")
    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    private String id;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Override
    public boolean isNew() {
        return id == null;
    }
}
