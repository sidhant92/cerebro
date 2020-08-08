package com.ant.search.cerebro.constant;

import java.util.Objects;
import com.ant.search.cerebro.domain.search.query.GeoAroundCenterQuery;
import com.ant.search.cerebro.domain.search.query.Query;
import com.ant.search.cerebro.dto.request.SearchRequest;
import com.ant.search.cerebro.exception.Error;
import com.ant.search.cerebro.service.index.field.fixed.GeoLocation;

/**
 * @author sidhant.aggarwal
 * @since 08/08/2020
 */
public enum GeoQueryType {
    AROUND_RADIUS {
        @Override
        public Query getQuery(final SearchRequest request) {
            validate(request);
            return GeoAroundCenterQuery.builder().field(GeoLocation.FIELD_NAME).maxRadius(request.getMaxRadius())
                                       .centerLatitude(request.getCenterPointLatitude()).centerLongitude(request.getCenterPointLongitude()).build();
        }

        private void validate(final SearchRequest request) {
            if (Objects.isNull(request.getMaxRadius()) || Objects.isNull(request.getCenterPointLatitude()) || Objects
                    .isNull(request.getCenterPointLongitude())) {
                throw Error.invalid_request.getBuilder().build();
            }
        }
    };

    public abstract Query getQuery(final SearchRequest request);
}
