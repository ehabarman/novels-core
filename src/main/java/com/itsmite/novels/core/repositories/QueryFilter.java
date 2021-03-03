package com.itsmite.novels.core.repositories;
import org.springframework.data.mongodb.core.query.Query;
public interface QueryFilter {

    public Query buildQuery();

}
