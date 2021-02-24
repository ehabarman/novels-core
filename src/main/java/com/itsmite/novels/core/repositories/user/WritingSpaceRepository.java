package com.itsmite.novels.core.repositories.user;

import com.itsmite.novels.core.models.user.WritingSpace;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WritingSpaceRepository extends MongoRepository<WritingSpace, String> {
}
