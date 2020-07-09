package com.ant.search.cerebro.service.datatype;

import com.ant.search.cerebro.constant.DataType;

/**
 * @author sidhant.aggarwal
 * @since 08/07/2020
 */
public abstract class PrimitiveAbstractType {
    public abstract DataType getType();

    public abstract boolean isValid(final Object value);
}
