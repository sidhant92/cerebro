package com.ant.search.cerebro.service.parser.filter.boolparser;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.constant.QueryClause;
import com.ant.search.cerebro.domain.search.query.BoolQuery;
import com.ant.search.cerebro.domain.search.query.RangeQuery;
import com.ant.search.cerebro.domain.search.query.TermQuery;
import com.github.sidhant92.boolparser.domain.BoolExpression;
import com.github.sidhant92.boolparser.domain.Node;
import com.github.sidhant92.boolparser.domain.NumericRangeToken;
import com.github.sidhant92.boolparser.domain.NumericToken;
import com.github.sidhant92.boolparser.domain.StringToken;

/**
 * @author sidhant.aggarwal
 * @since 20/07/2020
 */
@Service
public class FilterAdaptor {
    public BoolQuery getFilterQuery(final Node node, final BoolQuery outerBoolQuery, final QueryClause queryClause) {
        final BoolQuery boolQuery = Optional.ofNullable(outerBoolQuery).orElse(new BoolQuery());
        switch (node.getNodeType()) {
            case STRING_TOKEN:
                processStringToken((StringToken) node, boolQuery, Optional.ofNullable(queryClause).orElse(QueryClause.MUST));
                break;
            case NUMERIC_TOKEN:
                processNumericToken((NumericToken) node, boolQuery, Optional.ofNullable(queryClause).orElse(QueryClause.MUST));
                break;
            case NUMERIC_RANGE_TOKEN:
                processNumericRangeToken((NumericRangeToken) node, boolQuery, Optional.ofNullable(queryClause).orElse(QueryClause.MUST));
                break;
            case BOOL_EXPRESSION:
                boolQuery.addQuery(processBooleanExpression((BoolExpression) node), Optional.ofNullable(queryClause).orElse(QueryClause.MUST));
                break;
        }
        return boolQuery;
    }

    private BoolQuery processBooleanExpression(final BoolExpression boolExpression) {
        final BoolQuery boolQuery = new BoolQuery();
        boolExpression.getAndOperations().forEach(op -> getFilterQuery(op, boolQuery, QueryClause.MUST));
        boolExpression.getOrOperations().forEach(op -> getFilterQuery(op, boolQuery, QueryClause.SHOULD));
        boolExpression.getNotOperations().forEach(op -> getFilterQuery(op, boolQuery, QueryClause.MUST_NOT));
        return boolQuery;
    }

    private void processStringToken(final StringToken stringToken, final BoolQuery boolQuery, final QueryClause queryClause) {
        boolQuery.addQuery(new TermQuery(stringToken.getField(), stringToken.getValue()), queryClause);
    }

    private void processNumericToken(final NumericToken numericToken, final BoolQuery boolQuery, final QueryClause queryClause) {
        switch (numericToken.getOperator()) {
            case EQUALS:
                boolQuery.addQuery(new TermQuery(numericToken.getField(), numericToken.getValue()), queryClause);
                break;
            case LESS_THAN:
                boolQuery.addQuery(
                        RangeQuery.builder().field(numericToken.getField()).upperBound(numericToken.getValue()).upperBoundInclusive(false).build(),
                        queryClause);
                break;
            case LESS_THAN_EQUAL:
                boolQuery.addQuery(
                        RangeQuery.builder().field(numericToken.getField()).upperBound(numericToken.getValue()).upperBoundInclusive(true).build(),
                        queryClause);
                break;
            case GREATER_THAN:
                boolQuery.addQuery(
                        RangeQuery.builder().field(numericToken.getField()).lowerBound(numericToken.getValue()).lowerBoundInclusive(false).build(),
                        queryClause);
                break;
            case GREATER_THAN_EQUAL:
                boolQuery.addQuery(
                        RangeQuery.builder().field(numericToken.getField()).lowerBound(numericToken.getValue()).lowerBoundInclusive(true).build(),
                        queryClause);
                break;
            case NOT_EQUAL:
                boolQuery.addQuery(new TermQuery(numericToken.getField(), numericToken.getValue()), QueryClause.MUST_NOT);
                break;
        }
    }

    private void processNumericRangeToken(final NumericRangeToken numericRangeToken, final BoolQuery boolQuery, final QueryClause queryClause) {
        boolQuery.addQuery(
                RangeQuery.builder().field(numericRangeToken.getField()).lowerBound(numericRangeToken.getFromValue()).lowerBoundInclusive(true)
                          .upperBound(numericRangeToken.getToValue()).upperBoundInclusive(true).build(), queryClause);
    }
}
