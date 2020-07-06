package com.ant.search.cerebro.domain.search.query;

import java.util.ArrayList;
import java.util.List;
import com.ant.search.cerebro.constant.QueryClause;
import com.ant.search.cerebro.constant.QueryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sidhant.aggarwal
 * @since 06/07/2020
 */
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BoolQuery extends Query {
    private final List<Query> shouldClauses = new ArrayList<>();

    private final List<Query> mustClauses = new ArrayList<>();

    private final List<Query> mustNotClauses = new ArrayList<>();

    @Override
    public QueryType getQueryType() {
        return QueryType.bool;
    }

    public void addQuery(final Query query, final QueryClause queryClause) {
        switch (queryClause) {
            case must:
                mustClauses.add(query);
                break;
            case should:
                shouldClauses.add(query);
                break;
            case must_not:
                mustNotClauses.add(query);
                break;
            default:
        }
    }
}
