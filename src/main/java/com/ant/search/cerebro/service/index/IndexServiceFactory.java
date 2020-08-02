package com.ant.search.cerebro.service.index;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.constant.IndexProvider;
import com.ant.search.cerebro.service.index.elastic.ElasticIndexService;

/**
 * @author sidhant.aggarwal
 * @since 04/07/2020
 */
@Service
public class IndexServiceFactory {
    @Autowired
    private ElasticIndexService elasticIndexService;

    private final Map<IndexProvider, IndexService> indexServiceMap = new HashMap<>();

    @PostConstruct
    public void register() {
        indexServiceMap.put(IndexProvider.ELASTIC, elasticIndexService);
    }

    public IndexService getIndexService(final IndexProvider indexProvider) {
        return indexServiceMap.get(indexProvider);
    }
}
