package com.ant.search.cerebro.domain.search.query;

import java.util.Objects;
import com.ant.search.cerebro.constant.QueryType;
import com.ant.search.cerebro.exception.Error;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sidhant.aggarwal
 * @since 08/07/2020
 */
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RangeQuery extends Query {
    private final String field;

    private final Object lowerBound;

    private final Object upperBound;

    private final Boolean lowerBoundInclusive;

    private final Boolean upperBoundInclusive;

    @Override
    public QueryType getQueryType() {
        return QueryType.RANGE;
    }

    public void validate() {
        if (Objects.isNull(lowerBound) && Objects.isNull(upperBound)) {
            throw Error.invalid_range_query.getBuilder().build();
        }
        if (!Objects.isNull(upperBound) && Objects.isNull(upperBoundInclusive)) {
            throw Error.invalid_range_query.getBuilder().build();
        }
        if (!Objects.isNull(lowerBound) && Objects.isNull(lowerBoundInclusive)) {
            throw Error.invalid_range_query.getBuilder().build();
        }
    }
}
