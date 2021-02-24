package com.itsmite.novels.core.repositories.user;

import com.itsmite.novels.core.models.user.ReadingSpace;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReadingSpaceRepository extends MongoRepository<ReadingSpace, String> {
}
