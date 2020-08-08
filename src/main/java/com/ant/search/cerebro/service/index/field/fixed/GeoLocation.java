package com.ant.search.cerebro.service.index.field.fixed;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.constant.DataType;
import com.ant.search.cerebro.exception.Error;
import com.ant.search.cerebro.service.datatype.PrimitiveDataTypeFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sidhant.aggarwal
 * @since 08/08/2020
 */
@Slf4j
@Service
public class GeoLocation extends FixedField {
    @Autowired
    private PrimitiveDataTypeFactory primitiveDataTypeFactory;

    public static final String FIELD_NAME = "_geo_location";

    @Override
    public void isValid(final Map<String, Object> document) {
        if (!document.containsKey(FIELD_NAME)) {
            return;
        }
        final Object data = document.get(FIELD_NAME);
        if (data instanceof List) {
            validateMultiGeoPoints((List) data);
        } else {
            validateSingleGeoPoint((Map<String, Object>) data);
        }
    }

    @Override
    public Map<String, Object> getElasticMapping() {
        final Map<String, Object> fieldProperties = new HashMap<>();
        fieldProperties.put("type", "geo_point");
        final Map<String, Object> mapping = new HashMap<>();
        mapping.put(FIELD_NAME, fieldProperties);
        return mapping;
    }

    @Override
    public String getKey() {
        return FIELD_NAME;
    }

    private void validateMultiGeoPoints(final List<Object> geoPoints) {
        geoPoints.forEach(geoPoint -> {
            if (geoPoint instanceof Map) {
                validateSingleGeoPoint((Map<String, Object>) geoPoint);
            } else {
                log.error("Geo location field is not valid");
                throw Error.document_not_valid.getBuilder().build();
            }
        });
    }

    private void validateSingleGeoPoint(final Map<String, Object> geoPoint) {
        final Object latitude = geoPoint.get("lat");
        final Object longitude = geoPoint.get("lon");
        if (!primitiveDataTypeFactory.getDataType(DataType.DECIMAL).isValid(latitude) || !primitiveDataTypeFactory.getDataType(DataType.DECIMAL)
                                                                                                                  .isValid(longitude)) {
            log.error("Geo location field is not valid");
            throw Error.document_not_valid.getBuilder().build();
        }
    }
}
