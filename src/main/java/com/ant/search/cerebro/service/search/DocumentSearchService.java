package com.ant.search.cerebro.service.search;

import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.domain.index.IndexSettings;
import com.ant.search.cerebro.dto.response.GetDocumentByIdResponse;
import com.ant.search.cerebro.exception.Error;
import com.ant.search.cerebro.service.IndexSettingsService;
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

    public Optional<GetDocumentByIdResponse> getDocumentById(final String indexName, final String id) {
        final IndexSettings indexSettings = indexSettingsService.get(indexName).orElseThrow(Error.index_settings_not_found.getBuilder()::build);
        final Optional<Map<String, Object>> document = searchServiceFactory.getIndexService(indexSettings.getIndexProvider())
                                                                           .getDocumentById(indexName, id);
        return document.map(doc -> GetDocumentByIdResponse.builder().document(doc).build());
    }
}
