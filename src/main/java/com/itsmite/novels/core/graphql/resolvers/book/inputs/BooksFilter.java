package com.itsmite.novels.core.graphql.resolvers.book.inputs;

import com.itsmite.novels.core.models.book.BookStatus;
import com.itsmite.novels.core.repositories.QueryFilter;
import com.itsmite.novels.core.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BooksFilter implements QueryFilter {

    private BookStatus status;

    private String ownerId;

    @Override
    public Query buildQuery() {
        Query query = new Query();
        if (status != null) {
            query.addCriteria(Criteria.where("status").in(status));
        }
        if (StringUtil.isNullOrWhiteSpace(ownerId)) {
            query.addCriteria(Criteria.where("ownerId").in(ownerId));
        }
        return query;
    }
}
