package com.ant.search.cerebro.service.search.elastic;

import java.util.Map;
import java.util.Optional;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.service.search.SearchService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sidhant.aggarwal
 * @since 05/07/2020
 */
@Service
@Slf4j
public class ElasticSearchService implements SearchService {
    @Autowired
    private RestHighLevelClient elasticClient;

    @Override
    public Optional<Map<String, Object>> getDocumentById(String indexName, String id) {
        final GetRequest getRequest = new GetRequest(indexName).id(id);
        try {
            final GetResponse getResponse = elasticClient.get(getRequest, RequestOptions.DEFAULT);
            return Optional.ofNullable(getResponse.getSource());
        } catch (final Exception ex) {
            log.error("Error in getting document by id with message {}", ex.getMessage());
        }
        return Optional.empty();
    }
}
