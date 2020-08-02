package com.ant.search.cerebro.domain.search.query;

import com.ant.search.cerebro.constant.QueryType;

/**
 * @author sidhant.aggarwal
 * @since 06/07/2020
 */
public abstract class Query {
    public abstract QueryType getQueryType();
}
