package com.ant.search.cerebro.service.search.analyzer;

import java.util.List;
import com.ant.search.cerebro.constant.AnalyzerType;

/**
 * @author sidhant.aggarwal
 * @since 06/07/2020
 */
public abstract class Analyzer {
    public abstract List<String> getTokens(final String text);

    public abstract AnalyzerType getType();
}
