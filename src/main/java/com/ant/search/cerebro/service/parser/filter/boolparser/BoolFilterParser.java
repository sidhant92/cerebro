package com.ant.search.cerebro.service.parser.filter.boolparser;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.domain.search.query.BoolQuery;
import com.ant.search.cerebro.exception.Error;
import com.ant.search.cerebro.service.parser.filter.FilterParser;
import com.github.sidhant92.boolparser.domain.Node;
import com.github.sidhant92.boolparser.parser.canopy.PEGBoolExpressionParser;
import com.google.common.base.Strings;

/**
 * @author sidhant.aggarwal
 * @since 20/07/2020
 */
@Service
public class BoolFilterParser implements FilterParser {
    @Autowired
    private FilterAdaptor filterAdaptor;

    @Override
    public Optional<BoolQuery> parse(final String filterString) {
        if (Strings.isNullOrEmpty(filterString)) {
            return Optional.empty();
        }
        final PEGBoolExpressionParser boolExpressionParser = new PEGBoolExpressionParser();
        final Optional<Node> optionalNode = boolExpressionParser.parseExpression(filterString);
        if (!optionalNode.isPresent()) {
            throw Error.invalid_filter_expression.getBuilder().build();
        }
        return Optional.of(filterAdaptor.getFilterQuery(optionalNode.get(), null, null));
    }
}
