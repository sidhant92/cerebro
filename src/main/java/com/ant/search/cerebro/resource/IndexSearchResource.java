package com.ant.search.cerebro.resource;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ant.search.cerebro.dto.request.SearchRequest;
import com.ant.search.cerebro.dto.response.DocumentSearchResponse;
import com.ant.search.cerebro.dto.response.GetDocumentByIdResponse;
import com.ant.search.cerebro.exception.Error;
import com.ant.search.cerebro.service.search.DocumentSearchService;

/**
 * @author sidhant.aggarwal
 * @since 05/07/2020
 */
@RestController
public class IndexSearchResource {
    @Autowired
    private DocumentSearchService documentSearchService;

    @GetMapping ("/v1/index/{indexName}/document/{id}")
    public GetDocumentByIdResponse getDocumentById(@PathVariable final String indexName, @PathVariable final String id) {
        return documentSearchService.getDocumentById(indexName, id).orElseThrow(Error.document_not_found.getBuilder()::build);
    }

    @GetMapping ("/v1/index/{indexName}/search")
    public DocumentSearchResponse searchDocuments(@PathVariable final String indexName, @Valid final SearchRequest searchRequest) {
        searchRequest.setIndexName(indexName);
        return documentSearchService.searchDocuments(searchRequest);
    }

    @PostMapping ("/v1/index/{indexName}/search")
    public DocumentSearchResponse searchDocumentsPost(@PathVariable final String indexName, @RequestBody @Valid final SearchRequest searchRequest) {
        searchRequest.setIndexName(indexName);
        return documentSearchService.searchDocuments(searchRequest);
    }
}
