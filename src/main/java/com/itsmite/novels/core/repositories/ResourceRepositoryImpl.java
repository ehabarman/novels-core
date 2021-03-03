package com.itsmite.novels.core.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.util.Assert;

import java.io.Serializable;

public class ResourceRepositoryImpl<T, I extends Serializable> extends SimpleMongoRepository<T, I> implements ResourceRepository<T, I> {

    private final MongoOperations        mongoOperations;
    private final MongoEntityInformation entityInformation;

    public ResourceRepositoryImpl(final MongoEntityInformation entityInformation, final MongoOperations mongoOperations) {
        super(entityInformation, mongoOperations);
        this.entityInformation = entityInformation;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Page<T> findAll(final Query query, final Pageable pageable) {
        Assert.notNull(query, "Query must not be null!");

        return new PageImpl<T>(
            mongoOperations.find(query.with(pageable), entityInformation.getJavaType(), entityInformation.getCollectionName()),
            pageable,
            mongoOperations.count(query, entityInformation.getJavaType(), entityInformation.getCollectionName())
        );
    }
}