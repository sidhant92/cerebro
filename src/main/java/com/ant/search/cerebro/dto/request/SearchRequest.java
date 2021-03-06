package com.ant.search.cerebro.dto.request;

import java.util.Collections;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import com.ant.search.cerebro.constant.GeoQueryType;
import com.ant.search.cerebro.constant.QueryStrategyType;
import com.ant.search.cerebro.constant.SortOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author sidhant.aggarwal
 * @since 06/07/2020
 */
@NoArgsConstructor
@Getter
@Setter
public class SearchRequest {
    private String query = "";

    private String indexName;

    @Min (value = 1)
    @Max (value = 50)
    private Long perPage = 10L;

    @Min (value = 1)
    private Long pageNo = 1L;

    private String filters;

    private List<String> queryFields = Collections.emptyList();

    private List<String> facetFields = Collections.emptyList();

    private Boolean returnFacets = false;

    private QueryStrategyType queryStrategy;

    private GeoQueryType geoQueryType;

    private Integer maxRadius;

    @Valid
    private CenterPoint centerPoint;

    @Valid
    private BoundingBox boundingBox;

    private String sortBy;

    private SortOrder sortOrder = SortOrder.DESC;

    private Boolean sortByDistance = false;
}
