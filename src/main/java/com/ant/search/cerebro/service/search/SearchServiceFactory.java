package com.ant.search.cerebro.service.search;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.constant.IndexProvider;
import com.ant.search.cerebro.service.search.elastic.ElasticSearchService;

/**
 * @author sidhant.aggarwal
 * @since 05/07/2020
 */
@Service
public class SearchServiceFactory {
    @Autowired
    private ElasticSearchService elasticSearchService;

    private final Map<IndexProvider, SearchService> searchServiceMap = new HashMap<>();

    @PostConstruct
    public void register() {
        searchServiceMap.put(IndexProvider.ELASTIC, elasticSearchService);
    }

    public SearchService getIndexService(final IndexProvider indexProvider) {
        return searchServiceMap.get(indexProvider);
    }
}
