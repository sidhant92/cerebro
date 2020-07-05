package com.ant.search.cerebro.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
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
}
