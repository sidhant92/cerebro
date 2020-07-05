package com.ant.search.cerebro.resource;

import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ant.search.cerebro.domain.index.IndexSettings;
import com.ant.search.cerebro.dto.AddDocumentRequest;
import com.ant.search.cerebro.exception.Error;
import com.ant.search.cerebro.service.IndexSettingsService;
import com.ant.search.cerebro.service.IndexingService;

/**
 * @author sidhant.aggarwal
 * @since 04/07/2020
 */
@RestController
public class IndexResource {
    @Autowired
    private IndexingService indexingService;

    @Autowired
    private IndexSettingsService indexSettingsService;

    @PostMapping ("/v1/index/{indexName}")
    public IndexSettings createIndex(@PathVariable final String indexName, @RequestBody @Valid final IndexSettings indexSettings) {
        indexSettings.setIndexName(indexName);
        return indexingService.initializeIndex(indexSettings);
    }

    @GetMapping ("/v1/index/{indexName}")
    public IndexSettings getIndexSettings(@PathVariable final String indexName) {
        return indexSettingsService.get(indexName).orElseThrow(Error.index_settings_not_found.getBuilder()::build);
    }

    @PatchMapping ("/v1/index/{indexName}")
    public IndexSettings updateIndex(@PathVariable final String indexName, @RequestBody final IndexSettings indexSettings) {
        indexSettings.setIndexName(indexName);
        return indexingService.updateIndexSettings(indexSettings);
    }

    @PutMapping ("/v1/index/{indexName}/document/{id}")
    public Map<String, Object> addDocument(@PathVariable final String indexName, @PathVariable final String id,
            @RequestBody @Valid final AddDocumentRequest request) {
        request.setIndexName(indexName);
        request.setId(id);
        return indexingService.addDocument(request);
    }

    @DeleteMapping ("/v1/index/{indexName}/document/{id}")
    public void deleteDocument(@PathVariable final String indexName, @PathVariable final String id) {
        indexingService.deleteDocument(indexName, id);
    }
}
