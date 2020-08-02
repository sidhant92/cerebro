package com.ant.search.cerebro.service.index.elastic;

import java.util.HashMap;
import java.util.Map;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.domain.index.IndexSettings;
import com.ant.search.cerebro.dto.request.AddDocumentRequest;
import com.ant.search.cerebro.exception.Error;
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

    @Autowired
    private CustomAnalyzers customAnalyzers;

    private void createIndex(final IndexSettings indexSettings) {
        final CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexSettings.getIndexName());
        createIndexRequest.settings(getIndexSettings(indexSettings));
        try {
            elasticClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (final Exception ex) {
            log.error("Error in creating index with message {}", ex.getMessage());
            throw Error.unknown_error_occurred.getBuilder().build();
        }
    }

    private Map<String, Object> getIndexSettings(final IndexSettings indexSettings) {
        final Map<String, Object> settings = new HashMap<>(customAnalyzers.getCustomAnalyzers());
        settings.put("index", getCoreIndexSettings(indexSettings));
        return settings;
    }

    private Map<String, Object> getCoreIndexSettings(final IndexSettings indexSettings) {
        final Map<String, Object> settings = new HashMap<>();
        settings.put("number_of_shards", indexSettings.getNoOfShards());
        settings.put("number_of_replicas", indexSettings.getNoOfReplicas());
        return settings;
    }

    @Override
    public void initializeIndex(final IndexSettings indexSettings) {
        createIndex(indexSettings);
        mappingService.putMapping(indexSettings);
    }

    @Override
    public void updateIndex(final IndexSettings indexSettings) {
        mappingService.putMapping(indexSettings);
    }

    @Override
    public void addDocument(final AddDocumentRequest request) {
        final IndexRequest indexRequest = new IndexRequest(request.getIndexName()).id(request.getId());
        indexRequest.source(request.getDocument());
        try {
            elasticClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (final Exception ex) {
            log.error("Error in indexing document with message {}", ex.getMessage());
            throw Error.unknown_error_occurred.getBuilder().build();
        }
    }

    @Override
    public void deleteDocument(final String indexName, final String id) {
        final DeleteRequest deleteRequest = new DeleteRequest(indexName, id);
        try {
            elasticClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (final Exception ex) {
            log.error("Error in indexing document with message {}", ex.getMessage());
        }
    }
}
