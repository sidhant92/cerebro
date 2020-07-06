package com.ant.search.cerebro.service.search.analyzer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.constant.AnalyzerType;

/**
 * @author sidhant.aggarwal
 * @since 06/07/2020
 */
@Service
public class StandardAnalyzer extends Analyzer {
    @Override
    public List<String> getTokens(final String text) {
        return Arrays.stream(text.split("\\s+")).map(String::toLowerCase).filter(t -> !EnglishAnalyzer.getDefaultStopSet().contains(t))
                     .collect(Collectors.toList());
    }

    @Override
    public AnalyzerType getType() {
        return AnalyzerType.standard;
    }
}
