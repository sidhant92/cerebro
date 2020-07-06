package com.ant.search.cerebro.service.search.query;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.constant.QueryClause;
import com.ant.search.cerebro.constant.QueryStrategyType;
import com.ant.search.cerebro.domain.index.StorageSettings;
import com.ant.search.cerebro.domain.search.query.BoolQuery;
import com.ant.search.cerebro.domain.search.query.Query;
import com.ant.search.cerebro.dto.request.SearchRequest;
import com.ant.search.cerebro.service.search.analyzer.Analyzer;
import com.ant.search.cerebro.service.search.analyzer.AnalyzerFactory;

/**
 * @author sidhant.aggarwal
 * @since 07/07/2020
 */
@Service
public class AnyMatchQueryStrategy extends QueryStrategy {
    @Autowired
    private AnalyzerFactory analyzerFactory;

    @Override
    public QueryStrategyType getQueryStrategyType() {
        return QueryStrategyType.any_match;
    }

    @Override
    public Query getQuery(final SearchRequest searchRequest, final StorageSettings storageSettings) {
        final BoolQuery boolQuery = new BoolQuery();
        if (Optional.ofNullable(searchRequest.getQuery()).orElse("").equals("")) {
            return boolQuery;
        }
        getQueryFields(searchRequest, storageSettings).forEach(field -> {
            final Analyzer analyzer = analyzerFactory.getAnalyzer(storageSettings.getAnalyzerType(field));
            final BoolQuery innerBoolQuery = new BoolQuery();
            getTermQueriesFromTokens(analyzer, searchRequest.getQuery(), field)
                    .forEach(termQuery -> innerBoolQuery.addQuery(termQuery, QueryClause.should));
            boolQuery.addQuery(innerBoolQuery, QueryClause.should);
        });

        return boolQuery;
    }
}
