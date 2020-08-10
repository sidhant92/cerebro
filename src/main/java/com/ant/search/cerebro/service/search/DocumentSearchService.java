package com.ant.search.cerebro.service.search;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.constant.DataType;
import com.ant.search.cerebro.constant.QueryClause;
import com.ant.search.cerebro.domain.index.FieldConfig;
import com.ant.search.cerebro.domain.index.IndexSettings;
import com.ant.search.cerebro.domain.search.query.BoolQuery;
import com.ant.search.cerebro.domain.search.query.Query;
import com.ant.search.cerebro.dto.internal.DocumentSearchRequest;
import com.ant.search.cerebro.dto.request.SearchRequest;
import com.ant.search.cerebro.dto.response.DocumentSearchResponse;
import com.ant.search.cerebro.dto.response.GetDocumentByIdResponse;
import com.ant.search.cerebro.exception.Error;
import com.ant.search.cerebro.service.IndexSettingsService;
import com.ant.search.cerebro.service.parser.filter.boolparser.BoolFilterParser;
import com.ant.search.cerebro.service.search.query.QueryStrategyFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sidhant.aggarwal
 * @since 05/07/2020
 */
@Service
@Slf4j
public class DocumentSearchService {
    @Autowired
    private IndexSettingsService indexSettingsService;

    @Autowired
    private SearchServiceFactory searchServiceFactory;

    @Autowired
    private QueryStrategyFactory queryStrategyFactory;

    @Autowired
    private BoolFilterParser filterParser;

    public Optional<GetDocumentByIdResponse> getDocumentById(final String indexName, final String id) {
        final IndexSettings indexSettings = indexSettingsService.getCached(indexName).orElseThrow(Error.index_settings_not_found.getBuilder()::build);
        final Optional<Map<String, Object>> document = searchServiceFactory.getIndexService(indexSettings.getIndexProvider())
                                                                           .getDocumentById(indexName, id);
        return document.map(doc -> GetDocumentByIdResponse.builder().document(doc).build());
    }

    public DocumentSearchResponse searchDocuments(final SearchRequest request) {
        final IndexSettings indexSettings = indexSettingsService.getCached(request.getIndexName())
                                                                .orElseThrow(Error.index_settings_not_found.getBuilder()::build);
        final Long limit = request.getPerPage();
        final Long offset = (request.getPageNo() - 1) * request.getPerPage();
        final Optional<? extends Query> filters = getFilterQuery(request);
        validateSort(request);
        final Query query = queryStrategyFactory
                .getQueryStrategy(Optional.ofNullable(request.getQueryStrategy()).orElse(indexSettings.getSearchSettings().getQueryStrategy()))
                .getQuery(request, indexSettings.getStorageSettings());
        final DocumentSearchRequest documentSearchRequest = DocumentSearchRequest.builder().limit(limit).offset(offset)
                                                                                 .indexName(request.getIndexName()).query(query)
                                                                                 .filters(filters.orElse(null))
                                                                                 .computeFacets(request.getReturnFacets())
                                                                                 .facetFields(request.getFacetFields()).sortBy(request.getSortBy())
                                                                                 .sortOrder(request.getSortOrder())
                                                                                 .sortByDistance(request.getSortByDistance())
                                                                                 .centerPoint(request.getCenterPoint()).build();
        return searchServiceFactory.getIndexService(indexSettings.getIndexProvider()).searchDocuments(documentSearchRequest);
    }

    private void validateSort(final SearchRequest request) {
        if (request.getSortByDistance()) {
            if (Objects.isNull(request.getCenterPoint())) {
                throw Error.invalid_sort_param.getBuilder().build();
            }
        }
        final IndexSettings indexSettings = indexSettingsService.getCached(request.getIndexName())
                                                                .orElseThrow(Error.index_settings_not_found.getBuilder()::build);
        final FieldConfig fieldConfig = Optional.ofNullable(indexSettings.getStorageSettings().getFlattenedFieldConfigMap().get(request.getSortBy()))
                                                .orElseThrow(Error.invalid_sort_param.getBuilder()::build);
        if (fieldConfig.getDataType() == DataType.STRING && fieldConfig.getTokenize()) {
            throw Error.invalid_sort_param.getBuilder().build();
        }
    }

    private Optional<? extends Query> getFilterQuery(final SearchRequest searchRequest) {
        final Optional<BoolQuery> filters = filterParser.parse(searchRequest.getFilters());
        if (filters.isPresent()) {
            getGeoQuery(searchRequest).ifPresent(geoQuery -> filters.get().addQuery(geoQuery, QueryClause.MUST));
            return filters;
        } else {
            return getGeoQuery(searchRequest);
        }
    }

    private Optional<Query> getGeoQuery(final SearchRequest searchRequest) {
        return Optional.ofNullable(searchRequest.getGeoQueryType()).map(queryType -> queryType.getQuery(searchRequest));
    }
}
