package com.ant.search.cerebro.service.index.elastic;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.domain.index.IndexSettings;
import com.ant.search.cerebro.service.index.IndexService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sidhant.aggarwal
 * @since 04/07/2020
 */
@Service
@Slf4j
public class ElasticIndexService implements IndexService {
    @Autowired
    private MappingService mappingService;

    @Autowired
    private RestHighLevelClient elasticClient;

    private void createIndex(final String indexName) {
        final CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        //createIndexRequest.settings(getIndexSettings());
        try {
            elasticClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (final Exception ex) {
            log.error("Error in creating index with message {}", ex.getMessage());
        }
    }

    @Override
    public void initializeIndex(final IndexSettings indexSettings) {
        createIndex(indexSettings.getIndexName());
        mappingService.putMapping(indexSettings);
    }
}