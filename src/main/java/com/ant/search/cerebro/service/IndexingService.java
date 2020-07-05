package com.ant.search.cerebro.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.domain.index.IndexSettings;
import com.ant.search.cerebro.dto.AddDocumentRequest;
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

    public void initializeIndex(final IndexSettings indexSettings) {
        indexSettingsService.create(indexSettings);
        indexServiceFactory.getIndexService(indexSettings.getIndexProvider()).initializeIndex(indexSettings);
    }

    public Map<String, Object> addDocument(final AddDocumentRequest request) {
        final IndexSettings indexSettings = indexSettingsService.get(request.getIndexName()).get();
        final Map<String, Object> cleanedDocument = documentIndexer.cleanAndValidateDocument(request.getDocument(), request.getIndexName());
        request.setDocument(cleanedDocument);
        indexServiceFactory.getIndexService(indexSettings.getIndexProvider()).addDocument(request);
        return cleanedDocument;
    }
}
