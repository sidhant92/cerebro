package com.ant.search.cerebro.service.index.field.fixed;

import java.util.Map;

/**
 * @author sidhant.aggarwal
 * @since 08/08/2020
 */
public abstract class FixedField {
    public abstract void isValid(final Map<String, Object> document);

    public abstract Map<String, Object> getElasticMapping();

    public abstract String getKey();
}
