package com.ant.search.cerebro.service.datatype;

import java.util.Objects;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.constant.DataType;

/**
 * @author sidhant.aggarwal
 * @since 08/07/2020
 */
@Service
public class BoolDataType extends PrimitiveAbstractType {
    @Override
    public DataType getType() {
        return DataType.bool;
    }

    @Override
    public boolean isValid(final Object value) {
        return !Objects.isNull(value) && value instanceof Boolean && ("true".equalsIgnoreCase(String.valueOf(value)) || "false"
                .equalsIgnoreCase(String.valueOf(value)));
    }
}
