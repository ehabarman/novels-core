package com.itsmite.novels.core.graphql.resolvers.book.inputs;

import com.itsmite.novels.core.models.book.BookStatus;
import com.itsmite.novels.core.repositories.QueryFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.validation.constraints.NotEmpty;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BooksFilter implements QueryFilter {

    private BookStatus status;

    @NotEmpty
    private String ownerId;

    @Override
    public Query buildQuery() {
        Query query = new Query();
        if (status != null) {
            query.addCriteria(Criteria.where("status").in(status));
        }
        if (ownerId != null) {
            query.addCriteria(Criteria.where("ownerId").in(ownerId));
        }
        return query;
    }
}
