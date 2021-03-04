package com.itsmite.novels.core.graphql.resolvers.base.types;

public interface ObjectType<T> {

    T getEntity();

    void setEntity(T entity);

}
