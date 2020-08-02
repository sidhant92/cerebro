package com.ant.search.cerebro.service.index;

import com.ant.search.cerebro.domain.index.IndexSettings;
import com.ant.search.cerebro.dto.request.AddDocumentRequest;

/**
 * @author sidhant.aggarwal
 * @since 04/07/2020
 */
public interface IndexService {
    void initializeIndex(final IndexSettings indexSettings);

    void updateIndex(final IndexSettings indexSettings);

    void addDocument(final AddDocumentRequest request);

    void deleteDocument(final String indexName, final String id);
}
