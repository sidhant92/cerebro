package com.ant.search.cerebro.service.parser.filter.canopy;

import java.util.Optional;
import org.elasticsearch.common.Strings;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.domain.search.query.BoolQuery;
import com.ant.search.cerebro.service.parser.filter.FilterParser;
import com.ant.search.cerebro.service.parser.filter.canopy.domain.Queries;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sidhant.aggarwal
 * @since 27/05/19
 */
@Slf4j
@Service
public class CanopyFilterParser implements FilterParser {
    private final Actions actionsImpl;

    public CanopyFilterParser() {
        actionsImpl = new ActionsImpl();
    }

    @Override
    public Optional<BoolQuery> parse(String filterString) {
        try {
            if (Strings.isNullOrEmpty(filterString)) {
                return Optional.empty();
            }
            final TreeNode node = Filters.parse(filterString, actionsImpl);
            if (node instanceof Queries) {
                return Optional.of(new FilterAdaptor().getFilterQuery((Queries) node));
            }
        } catch (final Exception ex) {
            log.error("Error parsing filter query string");
        }
        return Optional.empty();
    }
}
