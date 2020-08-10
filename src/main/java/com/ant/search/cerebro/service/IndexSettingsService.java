package com.ant.search.cerebro.service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.configuration.InMemoryCacheConfiguration;
import com.ant.search.cerebro.dao.IndexSettingsDao;
import com.ant.search.cerebro.domain.index.IndexSettings;
import com.ant.search.cerebro.exception.Error;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

/**
 * @author sidhant.aggarwal
 * @since 04/07/2020
 */
@Service
public class IndexSettingsService {
    private IndexSettingsDao indexSettingsDao;

    private InMemoryCacheConfiguration inMemoryCacheConfiguration;

    private LoadingCache<String, Optional<IndexSettings>> indexSettingsCache;

    @Autowired
    public IndexSettingsService(final IndexSettingsDao indexSettingsDao, final InMemoryCacheConfiguration inMemoryCacheConfiguration) {
        this.indexSettingsDao = indexSettingsDao;
        this.inMemoryCacheConfiguration = inMemoryCacheConfiguration;
        this.indexSettingsCache = Caffeine.newBuilder().maximumSize(inMemoryCacheConfiguration.getCacheConfig().getCacheMaxSize())
                                          .expireAfterWrite(inMemoryCacheConfiguration.getCacheConfig().getExpiryAfterWriteInMinutes(),
                                                  TimeUnit.MINUTES)
                                          .refreshAfterWrite(inMemoryCacheConfiguration.getCacheConfig().getRefreshAfterWriteInMinutes(),
                                                  TimeUnit.MINUTES).build(this::get);
    }

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
        existingSettings.preUpdate();
        indexSettingsDao.updateOrAdd(existingSettings);
        indexSettingsCache.refresh(indexSettings.getIndexName());
        return existingSettings;
    }

    public Optional<IndexSettings> get(final String indexName) {
        return indexSettingsDao.getByName(indexName);
    }

    public Optional<IndexSettings> getCached(final String indexName) {
        return this.indexSettingsCache.get(indexName);
    }
}
