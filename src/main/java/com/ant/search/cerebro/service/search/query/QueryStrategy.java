package com.ant.search.cerebro.service.search.query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.ant.search.cerebro.constant.QueryStrategyType;
import com.ant.search.cerebro.domain.index.StorageSettings;
import com.ant.search.cerebro.domain.search.query.Query;
import com.ant.search.cerebro.domain.search.query.TermQuery;
import com.ant.search.cerebro.dto.request.SearchRequest;
import com.ant.search.cerebro.service.search.analyzer.Analyzer;

/**
 * @author sidhant.aggarwal
 * @since 06/07/2020
 */
public abstract class QueryStrategy {
    public abstract Query getQuery(final SearchRequest searchRequest, final StorageSettings storageSettings);

    public abstract QueryStrategyType getQueryStrategyType();

    protected List<String> getQueryFields(final SearchRequest searchRequest, final StorageSettings storageSettings) {
        final List<String> queryFields = searchRequest.getQueryFields().isEmpty() ? new ArrayList<>(
                storageSettings.getFlattenedFieldConfigMap().keySet()) : searchRequest.getQueryFields();
        return storageSettings.filterSearchableStringFields(queryFields);
    }

    protected List<TermQuery> getTermQueriesFromTokens(final Analyzer analyzer, final String query, final String field) {
        return analyzer.getTokens(query).stream().map(token -> new TermQuery(field, token)).collect(Collectors.toList());
    }
}
