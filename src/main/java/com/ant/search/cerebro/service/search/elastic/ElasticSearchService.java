package com.ant.search.cerebro.service.search.elastic;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.domain.index.IndexSettings;
import com.ant.search.cerebro.domain.search.query.BoolQuery;
import com.ant.search.cerebro.domain.search.query.Query;
import com.ant.search.cerebro.dto.internal.DocumentSearchRequest;
import com.ant.search.cerebro.dto.response.DocumentSearchResponse;
import com.ant.search.cerebro.exception.Error;
import com.ant.search.cerebro.service.IndexSettingsService;
import com.ant.search.cerebro.service.search.SearchService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sidhant.aggarwal
 * @since 05/07/2020
 */
@Service
@Slf4j
public class ElasticSearchService implements SearchService {
    @Autowired
    private RestHighLevelClient elasticClient;

    @Autowired
    private QueryAdaptor queryAdaptor;

    @Autowired
    private IndexSettingsService indexSettingsService;

    @Override
    public Optional<Map<String, Object>> getDocumentById(String indexName, String id) {
        final GetRequest getRequest = new GetRequest(indexName).id(id);
        try {
            final GetResponse getResponse = elasticClient.get(getRequest, RequestOptions.DEFAULT);
            return Optional.ofNullable(getResponse.getSource());
        } catch (final Exception ex) {
            log.error("Error in getting document by id with message {}", ex.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public DocumentSearchResponse searchDocuments(final DocumentSearchRequest request) {
        final SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(makeSearchQuery(request));
        searchRequest.indices(request.getIndexName());

        try {
            final SearchResponse searchResponse = elasticClient.search(searchRequest, RequestOptions.DEFAULT);
            return mapToSearchResponse(request, searchResponse);
        } catch (final Exception ex) {
            log.error("Error in searching documents with message {} and error", ex.getMessage(), ex);
            throw Error.unknown_error_occurred.getBuilder().build();
        }
    }

    private SearchSourceBuilder makeSearchQuery(final DocumentSearchRequest request) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        setQuery(request, searchSourceBuilder);
        setFilters(searchSourceBuilder, request.getFilters());
        setAggregations(request, searchSourceBuilder);
        setPagination(request, searchSourceBuilder);
        return searchSourceBuilder;
    }

    private void setFilters(final SearchSourceBuilder searchSourceBuilder, final Query filterQuery) {
        if (!Objects.isNull(filterQuery)) {
            final QueryBuilder query = searchSourceBuilder.query();
            final QueryBuilder filter = queryAdaptor.getElasticQuery(filterQuery);
            final BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            boolQueryBuilder.must(query);
            boolQueryBuilder.filter(filter);
            searchSourceBuilder.query(boolQueryBuilder);
        }
    }

    private void setAggregations(final DocumentSearchRequest request, final SearchSourceBuilder searchSourceBuilder) {
        if (!request.getComputeFacets()) {
            return;
        }
        final IndexSettings indexSettings = indexSettingsService.getCached(request.getIndexName())
                                                                .orElseThrow(Error.index_settings_not_found.getBuilder()::build);
        final List<String> aggregationFields = indexSettings.getStorageSettings().getFlattenedFieldConfigMap().entrySet().stream()
                                                            .filter(entry -> entry.getValue().getFacetRequired()).map(Map.Entry::getKey)
                                                            .collect(Collectors.toList());
        final List<String> facetFields = request.getFacetFields().isEmpty() ? aggregationFields : request.getFacetFields().stream()
                                                                                                         .filter(aggregationFields::contains)
                                                                                                         .collect(Collectors.toList());
        facetFields.forEach(aggregationField -> searchSourceBuilder.aggregation(
                AggregationBuilders.terms(aggregationField).field(aggregationField).size(indexSettings.getSearchSettings().getMaxValuesPerFacet())));
    }

    private void setQuery(final DocumentSearchRequest request, final SearchSourceBuilder searchSourceBuilder) {
        searchSourceBuilder.query(queryAdaptor.getElasticQuery(request.getQuery()));
    }

    private void setPagination(final DocumentSearchRequest request, final SearchSourceBuilder searchSourceBuilder) {
        searchSourceBuilder.size(request.getLimit().intValue());
        searchSourceBuilder.from(request.getOffset().intValue());
    }

    private DocumentSearchResponse mapToSearchResponse(final DocumentSearchRequest request, final SearchResponse response) {
        List<Map<String, Object>> results = Arrays.stream(response.getHits().getHits()).map(SearchHit::getSourceAsMap).collect(Collectors.toList());
        final Boolean nextPage = response.getHits().getTotalHits().value > request.getLimit() + request.getOffset();
        return DocumentSearchResponse.builder().documents(results).nextPage(nextPage).totalHits(response.getHits().getTotalHits().value)
                                     .facets(getFacets(response).orElse(null)).build();
    }

    private Optional<Map<String, Map<String, Long>>> getFacets(final SearchResponse response) {
        if (Objects.isNull(response.getAggregations())) {
            return Optional.empty();
        }
        final Map<String, Aggregation> aggregations = response.getAggregations().asMap();
        return Optional.of(aggregations.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> mapAggregation(e.getValue()))));
    }

    private Map<String, Long> mapAggregation(final Aggregation aggregation) {
        final Terms terms = (Terms) aggregation;
        return terms.getBuckets().stream().collect(Collectors
                .toMap(MultiBucketsAggregation.Bucket::getKeyAsString, MultiBucketsAggregation.Bucket::getDocCount, (x, y) -> y, LinkedHashMap::new));
    }
}
