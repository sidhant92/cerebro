package com.ant.search.cerebro.service.search;

import java.util.Map;
import java.util.Optional;
import com.ant.search.cerebro.dto.internal.DocumentSearchRequest;
import com.ant.search.cerebro.dto.response.DocumentSearchResponse;

/**
 * @author sidhant.aggarwal
 * @since 05/07/2020
 */
public interface SearchService {
    Optional<Map<String, Object>> getDocumentById(final String indexName, final String id);

    DocumentSearchResponse searchDocuments(final DocumentSearchRequest request);
}
