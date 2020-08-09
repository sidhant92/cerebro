package com.ant.search.cerebro.domain.search.query;

import com.ant.search.cerebro.constant.QueryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sidhant.aggarwal
 * @since 09/08/2020
 */
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GeoBoundingBoxQuery extends Query {
    private final String field;

    private final Double topLeftLatitude;

    private final Double topLeftLongitude;

    private final Double bottomRightLatitude;

    private final Double bottomRightLongitude;

    @Override
    public QueryType getQueryType() {
        return QueryType.GEO_BOUNDING_BOX;
    }
}
