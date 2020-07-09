package com.ant.search.cerebro.service.search.elastic;

import java.util.Optional;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.domain.search.query.BoolQuery;
import com.ant.search.cerebro.domain.search.query.Query;
import com.ant.search.cerebro.domain.search.query.RangeQuery;
import com.ant.search.cerebro.domain.search.query.TermQuery;
import com.ant.search.cerebro.exception.Error;

/**
 * @author sidhant.aggarwal
 * @since 06/07/2020
 */
@Service
public class QueryAdaptor {
    public QueryBuilder getElasticQuery(final Query query) {
        switch (query.getQueryType()) {
            case term:
            case range:
                return getPrimitiveQuery(query);
            case bool:
                return getBooleanQuery(query);
            default:
                throw Error.unknown_query_type.getBuilder().build();
        }
    }

    private QueryBuilder getBooleanQuery(final Query query) {
        final BoolQuery boolQuery = (BoolQuery) query;
        final BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQuery.getMustClauses().forEach(clause -> {
            final QueryBuilder queryBuilder = getElasticQuery(clause);
            boolQueryBuilder.must(queryBuilder);
        });
        boolQuery.getShouldClauses().forEach(clause -> {
            final QueryBuilder queryBuilder = getElasticQuery(clause);
            boolQueryBuilder.should(queryBuilder);
        });
        boolQuery.getMustNotClauses().forEach(clause -> {
            final QueryBuilder queryBuilder = getElasticQuery(clause);
            boolQueryBuilder.mustNot(queryBuilder);
        });
        return boolQueryBuilder;
    }

    private QueryBuilder getPrimitiveQuery(final Query query) {
        switch (query.getQueryType()) {
            case term:
                final TermQuery termQuery = (TermQuery) query;
                return QueryBuilders.termQuery(termQuery.getField(), termQuery.getValue());
            case range:
                return rangeQueryConverter((RangeQuery) query);
            default:
                throw new RuntimeException();
        }
    }

    private QueryBuilder rangeQueryConverter(final RangeQuery rangeQuery) {
        rangeQuery.validate();
        final RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(rangeQuery.getField());
        Optional.ofNullable(rangeQuery.getLowerBound())
                .ifPresent(lowerBound -> rangeQueryBuilder.from(lowerBound, rangeQuery.getLowerBoundInclusive()));
        Optional.ofNullable(rangeQuery.getUpperBound())
                .ifPresent(upperBound -> rangeQueryBuilder.to(upperBound, rangeQuery.getUpperBoundInclusive()));
        return rangeQueryBuilder;
    }
}
