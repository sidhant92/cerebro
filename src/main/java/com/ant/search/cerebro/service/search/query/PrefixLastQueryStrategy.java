package com.ant.search.cerebro.service.search.query;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.constant.QueryClause;
import com.ant.search.cerebro.constant.QueryStrategyType;
import com.ant.search.cerebro.domain.index.StorageSettings;
import com.ant.search.cerebro.domain.search.query.BoolQuery;
import com.ant.search.cerebro.domain.search.query.Query;
import com.ant.search.cerebro.domain.search.query.TermQuery;
import com.ant.search.cerebro.dto.request.SearchRequest;
import com.ant.search.cerebro.service.search.analyzer.Analyzer;
import com.ant.search.cerebro.service.search.analyzer.AnalyzerFactory;

/**
 * @author sidhant.aggarwal
 * @since 07/07/2020
 */
@Service
public class PrefixLastQueryStrategy extends QueryStrategy {
    @Autowired
    private AnalyzerFactory analyzerFactory;

    @Value ("${autocomplete.sub_field_suffix}")
    private String autocompleteSubFieldSuffix;

    @Override
    public Query getQuery(final SearchRequest searchRequest, final StorageSettings storageSettings) {
        final BoolQuery boolQuery = new BoolQuery();
        if (searchRequest.getQuery().equals("")) {
            return boolQuery;
        }
        getQueryFields(searchRequest, storageSettings).forEach(field -> {
            final Analyzer analyzer = analyzerFactory.getAnalyzer(storageSettings.getAnalyzerType(field));
            final BoolQuery innerBoolQuery = new BoolQuery();
            final List<TermQuery> termQueries = getTermQueriesFromTokens(analyzer, searchRequest.getQuery(), field);
            final TermQuery lastTermQuery = new TermQuery(getAutocompleteFieldName(field, autocompleteSubFieldSuffix),
                    termQueries.get(termQueries.size() - 1).getValue());
            final List<TermQuery> termQueriesButLast = termQueries.subList(0, termQueries.size() - 1);
            termQueriesButLast.forEach(termQuery -> innerBoolQuery.addQuery(termQuery, QueryClause.MUST));
            innerBoolQuery.addQuery(lastTermQuery, QueryClause.MUST);
            boolQuery.addQuery(innerBoolQuery, QueryClause.SHOULD);
        });

        return boolQuery;
    }

    private String getAutocompleteFieldName(final String field, final String autocompleteFieldSuffix) {
        return field + "." + autocompleteFieldSuffix;
    }

    @Override
    public QueryStrategyType getQueryStrategyType() {
        return QueryStrategyType.PREFIX_LAST;
    }
}
