package com.ant.search.cerebro.service.parser.filter;

import java.util.Optional;
import com.ant.search.cerebro.domain.search.query.BoolQuery;

/**
 * @author sidhant.aggarwal
 * @since 27/05/19
 */
public interface FilterParser {
    Optional<BoolQuery> parse(final String filterString);
}
