package com.ant.search.cerebro.domain.search.query;

import com.ant.search.cerebro.constant.QueryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sidhant.aggarwal
 * @since 08/08/2020
 */
@AllArgsConstructor
@Getter
@Setter
@Builder
public class GeoAroundCenterQuery extends Query {
    private final String field;

    private final Double centerLatitude;

    private final Double centerLongitude;

    private final Integer maxRadius;

    @Override
    public QueryType getQueryType() {
        return QueryType.GEO_AROUND_CENTER;
    }
}
