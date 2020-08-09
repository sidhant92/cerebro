package com.ant.search.cerebro.constant;

import java.util.Objects;
import com.ant.search.cerebro.domain.search.query.GeoAroundCenterQuery;
import com.ant.search.cerebro.domain.search.query.GeoBoundingBoxQuery;
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
                                       .centerLatitude(request.getCenterPoint().getLat()).centerLongitude(request.getCenterPoint().getLon()).build();
        }

        private void validate(final SearchRequest request) {
            if (Objects.isNull(request.getMaxRadius()) || Objects.isNull(request.getCenterPoint())) {
                throw Error.invalid_request.getBuilder().build();
            }
        }
    },
    BOUNDING_BOX {
        @Override
        public Query getQuery(final SearchRequest request) {
            validate(request);
            return GeoBoundingBoxQuery.builder().field(GeoLocation.FIELD_NAME).topLeftLatitude(request.getBoundingBox().getTopLeftLat())
                                      .topLeftLongitude(request.getBoundingBox().getTopLeftLon())
                                      .bottomRightLatitude(request.getBoundingBox().getBottomRightLat())
                                      .bottomRightLongitude(request.getBoundingBox().getBottomRightLon()).build();
        }

        private void validate(final SearchRequest request) {
            if (Objects.isNull(request.getBoundingBox())) {
                throw Error.invalid_request.getBuilder().build();
            }
        }
    };

    public abstract Query getQuery(final SearchRequest request);
}
