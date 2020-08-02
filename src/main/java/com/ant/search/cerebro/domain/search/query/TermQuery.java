package com.ant.search.cerebro.domain.search.query;

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
public class TermQuery extends Query {
    private final String field;

    private final Object value;

    @Override
    public QueryType getQueryType() {
        return QueryType.TERM;
    }
}
