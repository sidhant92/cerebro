package com.ant.search.cerebro.dto.internal;

import java.util.List;
import com.ant.search.cerebro.constant.SortOrder;
import com.ant.search.cerebro.domain.search.query.BoolQuery;
import com.ant.search.cerebro.domain.search.query.Query;
import com.ant.search.cerebro.dto.request.CenterPoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author sidhant.aggarwal
 * @since 06/07/2020
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DocumentSearchRequest {
    private Query query;

    private String indexName;

    private Long offset;

    private Long limit;

    private Query filters;

    private Boolean computeFacets;

    private List<String> facetFields;

    private String sortBy;

    private SortOrder sortOrder;

    private Boolean sortByDistance = false;

    private CenterPoint centerPoint;
}
