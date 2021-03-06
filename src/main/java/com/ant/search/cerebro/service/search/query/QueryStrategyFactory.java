package com.ant.search.cerebro.service.search.query;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.constant.QueryStrategyType;

/**
 * @author sidhant.aggarwal
 * @since 06/07/2020
 */
@Service
public class QueryStrategyFactory {
    @Autowired
    private AllMatchQueryStrategy allMatchQueryStrategy;

    @Autowired
    private AnyMatchQueryStrategy anyMatchQueryStrategy;

    @Autowired
    private PrefixLastQueryStrategy prefixLastQueryStrategy;

    private final Map<QueryStrategyType, QueryStrategy> queryStrategyMap = new HashMap<>();

    @PostConstruct
    public void register() {
        queryStrategyMap.put(QueryStrategyType.ALL_MATCH, allMatchQueryStrategy);
        queryStrategyMap.put(QueryStrategyType.ANY_MATCH, anyMatchQueryStrategy);
        queryStrategyMap.put(QueryStrategyType.PREFIX_LAST, prefixLastQueryStrategy);
    }

    public QueryStrategy getQueryStrategy(final QueryStrategyType queryStrategyType) {
        return queryStrategyMap.get(queryStrategyType);
    }
}
