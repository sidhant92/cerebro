package com.ant.search.cerebro.service.search;

import java.util.Map;
import java.util.Optional;

/**
 * @author sidhant.aggarwal
 * @since 05/07/2020
 */
public interface SearchService {
    Optional<Map<String, Object>> getDocumentById(final String indexName, final String id);
}
