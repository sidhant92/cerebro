package com.ant.search.cerebro.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.dao.IndexSettingsDao;
import com.ant.search.cerebro.domain.index.IndexSettings;
import com.ant.search.cerebro.exception.Error;

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
            throw Error.index_already_present.getBuilder().build();
        });
        indexSettings.prePersist();
        indexSettingsDao.updateOrAdd(indexSettings);
        return indexSettings;
    }

    public IndexSettings update(final IndexSettings indexSettings) {
        final IndexSettings existingSettings = get(indexSettings.getIndexName()).orElseThrow(Error.index_settings_not_found.getBuilder()::build);
        existingSettings.merge(indexSettings);
        indexSettings.preUpdate();
        indexSettingsDao.updateOrAdd(existingSettings);
        return existingSettings;
    }

    public Optional<IndexSettings> get(final String indexName) {
        return indexSettingsDao.getByName(indexName);
    }
}
