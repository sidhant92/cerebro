package com.ant.search.cerebro.service.search.analyzer;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.constant.AnalyzerType;

/**
 * @author sidhant.aggarwal
 * @since 06/07/2020
 */
@Service
public class AnalyzerFactory {
    @Autowired
    private StandardAnalyzer standardAnalyzer;

    private final Map<AnalyzerType, Analyzer> analyzerMap = new HashMap<>();

    @PostConstruct
    public void register() {
        analyzerMap.put(AnalyzerType.standard, standardAnalyzer);
    }

    public Analyzer getAnalyzer(final AnalyzerType analyzerType) {
        return analyzerMap.get(analyzerType);
    }
}
