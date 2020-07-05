package com.ant.search.cerebro.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.domain.index.IndexSettings;
import com.ant.search.cerebro.dto.AddDocumentRequest;
import com.ant.search.cerebro.exception.Error;
import com.ant.search.cerebro.service.index.IndexServiceFactory;

/**
 * @author sidhant.aggarwal
 * @since 04/07/2020
 */
@Service
public class IndexingService {
    @Autowired
    private IndexServiceFactory indexServiceFactory;

    @Autowired
    private IndexSettingsService indexSettingsService;

    @Autowired
    private DocumentIndexer documentIndexer;

    public IndexSettings initializeIndex(final IndexSettings indexSettings) {
        final IndexSettings createdIndexingSettings = indexSettingsService.create(indexSettings);
        indexServiceFactory.getIndexService(indexSettings.getIndexProvider()).initializeIndex(indexSettings);
        return createdIndexingSettings;
    }

    public IndexSettings updateIndexSettings(final IndexSettings indexSettings) {
        return indexSettingsService.update(indexSettings);
    }

    public Map<String, Object> addDocument(final AddDocumentRequest request) {
        final IndexSettings indexSettings = indexSettingsService.get(request.getIndexName())
                                                                .orElseThrow(Error.index_settings_not_found.getBuilder()::build);
        final Map<String, Object> cleanedDocument = documentIndexer.cleanAndValidateDocument(request.getDocument(), request.getIndexName());
        request.setDocument(cleanedDocument);
        indexServiceFactory.getIndexService(indexSettings.getIndexProvider()).addDocument(request);
        return cleanedDocument;
    }

    public void deleteDocument(final String indexName, final String id) {
        final IndexSettings indexSettings = indexSettingsService.get(indexName).orElseThrow(Error.index_settings_not_found.getBuilder()::build);
        indexServiceFactory.getIndexService(indexSettings.getIndexProvider()).deleteDocument(indexName, id);
    }
}
