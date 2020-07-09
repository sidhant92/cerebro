package com.ant.search.cerebro.service.parser.filter.canopy;

import com.ant.search.cerebro.constant.QueryClause;
import com.ant.search.cerebro.domain.search.query.BoolQuery;
import com.ant.search.cerebro.domain.search.query.RangeQuery;
import com.ant.search.cerebro.domain.search.query.TermQuery;
import com.ant.search.cerebro.exception.Error;
import com.ant.search.cerebro.service.parser.filter.canopy.domain.NumericRangeToken;
import com.ant.search.cerebro.service.parser.filter.canopy.domain.NumericToken;
import com.ant.search.cerebro.service.parser.filter.canopy.domain.Queries;
import com.ant.search.cerebro.service.parser.filter.canopy.domain.Token;

/**
 * @author sidhant.aggarwal
 * @since 27/05/19
 */
class FilterAdaptor {
    BoolQuery getFilterQuery(final Queries queries) {
        final BoolQuery boolQuery = new BoolQuery();
        queries.getAndQueries().forEach(query -> processQuery(query, boolQuery, QueryClause.must));
        queries.getOrQueries().forEach(query -> processQuery(query, boolQuery, QueryClause.should));
        return boolQuery;
    }

    void processQuery(final TreeNode treeNode, final BoolQuery boolQuery, final QueryClause queryClause) {
        if (treeNode instanceof Token) {
            final Token token = (Token) treeNode;
            final QueryClause finalQueryClause = token.getNotEnabled() ? QueryClause.must_not : queryClause;
            boolQuery.addQuery(new TermQuery(token.getField(), token.getValue()), finalQueryClause);
        } else if (treeNode instanceof NumericToken) {
            final NumericToken numericToken = (NumericToken) treeNode;
            final QueryClause finalQueryClause = numericToken.getNotEnabled() ? QueryClause.must_not : queryClause;
            if (numericToken.getOperator().equals("=")) {
                boolQuery.addQuery(new TermQuery(numericToken.getField(), numericToken.getValue()), finalQueryClause);
            } else {
                boolQuery.addQuery(parseRangeQuery(numericToken), finalQueryClause);
            }
        } else if (treeNode instanceof NumericRangeToken) {
            final NumericRangeToken numericRangeToken = (NumericRangeToken) treeNode;
            final QueryClause finalQueryClause = numericRangeToken.getNotEnabled() ? QueryClause.must_not : queryClause;
            boolQuery.addQuery(
                    RangeQuery.builder().field(numericRangeToken.getField()).lowerBound(numericRangeToken.getFromValue()).lowerBoundInclusive(true)
                              .upperBound(numericRangeToken.getToValue()).upperBoundInclusive(true).build(), finalQueryClause);
        } else {
            boolQuery.addQuery(getFilterQuery((Queries) treeNode), queryClause);
        }
    }

    RangeQuery parseRangeQuery(final NumericToken numericToken) {
        switch (numericToken.getOperator()) {
            case ">":
                return RangeQuery.builder().field(numericToken.getField()).lowerBound(numericToken.getValue()).lowerBoundInclusive(false).build();
            case ">=":
                return RangeQuery.builder().field(numericToken.getField()).lowerBound(numericToken.getValue()).lowerBoundInclusive(true).build();
            case "<":
                return RangeQuery.builder().field(numericToken.getField()).upperBound(numericToken.getValue()).upperBoundInclusive(false).build();
            case "<=":
                return RangeQuery.builder().field(numericToken.getField()).upperBound(numericToken.getValue()).upperBoundInclusive(true).build();
            default:
                throw Error.invalid_operator.getBuilder().build();
        }
    }
}
