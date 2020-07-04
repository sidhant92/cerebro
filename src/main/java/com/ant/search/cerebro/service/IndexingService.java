package com.ant.search.cerebro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.domain.index.IndexSettings;
import com.ant.search.cerebro.service.index.IndexServiceFactory;

/**
 * @author sidhant.aggarwal
 * @since 04/07/2020
 */
@Service
public class IndexingService {
    @Autowired
    private IndexServiceFactory indexServiceFactory;

    public void initializeIndex(final IndexSettings indexSettings) {
        indexServiceFactory.getIndexService(indexSettings.getIndexProvider()).initializeIndex(indexSettings);
    }
}
