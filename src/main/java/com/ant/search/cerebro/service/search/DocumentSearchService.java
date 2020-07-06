package com.ant.search.cerebro.service.search;

import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.domain.index.IndexSettings;
import com.ant.search.cerebro.domain.search.query.Query;
import com.ant.search.cerebro.dto.internal.DocumentSearchRequest;
import com.ant.search.cerebro.dto.request.SearchRequest;
import com.ant.search.cerebro.dto.response.DocumentSearchResponse;
import com.ant.search.cerebro.dto.response.GetDocumentByIdResponse;
import com.ant.search.cerebro.exception.Error;
import com.ant.search.cerebro.service.IndexSettingsService;
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

    public Optional<GetDocumentByIdResponse> getDocumentById(final String indexName, final String id) {
        final IndexSettings indexSettings = indexSettingsService.get(indexName).orElseThrow(Error.index_settings_not_found.getBuilder()::build);
        final Optional<Map<String, Object>> document = searchServiceFactory.getIndexService(indexSettings.getIndexProvider())
                                                                           .getDocumentById(indexName, id);
        return document.map(doc -> GetDocumentByIdResponse.builder().document(doc).build());
    }

    public DocumentSearchResponse searchDocuments(final SearchRequest request) {
        final IndexSettings indexSettings = indexSettingsService.get(request.getIndexName())
                                                                .orElseThrow(Error.index_settings_not_found.getBuilder()::build);
        final Long limit = request.getPerPage();
        final Long offset = (request.getPageNo() - 1) * request.getPerPage();
        final Query query = queryStrategyFactory.getQueryStrategy(request.getQueryStrategy()).getQuery(request, indexSettings.getStorageSettings());
        final DocumentSearchRequest documentSearchRequest = DocumentSearchRequest.builder().limit(limit).offset(offset)
                                                                                 .indexName(request.getIndexName()).query(query).build();
        return searchServiceFactory.getIndexService(indexSettings.getIndexProvider()).searchDocuments(documentSearchRequest);
    }
}
