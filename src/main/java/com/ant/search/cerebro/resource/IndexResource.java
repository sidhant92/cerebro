package com.ant.search.cerebro.resource;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ant.search.cerebro.domain.index.IndexSettings;
import com.ant.search.cerebro.service.IndexingService;

/**
 * @author sidhant.aggarwal
 * @since 04/07/2020
 */
@RestController
public class IndexResource {
    @Autowired
    private IndexingService indexingService;

    @PostMapping ("/v1/index/{indexName}")
    public IndexSettings createIndex(@PathVariable final String indexName, @RequestBody @Valid final IndexSettings indexSettings) {
        indexSettings.setIndexName(indexName);
        indexingService.initializeIndex(indexSettings);
        return indexSettings;
    }
}
