package com.ant.search.cerebro.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.dao.IndexSettingsDao;
import com.ant.search.cerebro.domain.index.IndexSettings;

/**
 * @author sidhant.aggarwal
 * @since 04/07/2020
 */
@Service
public class IndexSettingsService {
    @Autowired
    private IndexSettingsDao indexSettingsDao;

    public IndexSettings create(final IndexSettings indexSettings) {
        get(indexSettings.getIndexName()).ifPresent(a -> {
            //throw new Exception("Index Already Present");
        });
        indexSettingsDao.updateOrAdd(indexSettings);
        return indexSettings;
    }

    public Optional<IndexSettings> get(final String indexName) {
        return indexSettingsDao.getByName(indexName);
    }
}
