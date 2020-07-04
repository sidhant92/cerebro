package com.ant.search.cerebro.service.index;

import com.ant.search.cerebro.domain.index.IndexSettings;

/**
 * @author sidhant.aggarwal
 * @since 04/07/2020
 */
public interface IndexService {
    void initializeIndex(final IndexSettings indexSettings);
}
