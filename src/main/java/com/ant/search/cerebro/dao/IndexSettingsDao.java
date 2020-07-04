package com.ant.search.cerebro.dao;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.ant.search.cerebro.domain.index.IndexSettings;

/**
 * @author sidhant.aggarwal
 * @since 04/07/2020
 */
@Service
public class IndexSettingsDao {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public void updateOrAdd(final IndexSettings indexSettings) {
        dynamoDBMapper.save(indexSettings);
    }

    public void delete(final IndexSettings indexSettings) {
        dynamoDBMapper.delete(indexSettings);
    }

    public Optional<IndexSettings> getByName(final String indexName) {
        return Optional.ofNullable(dynamoDBMapper.load(IndexSettings.class, indexName));
    }
}
