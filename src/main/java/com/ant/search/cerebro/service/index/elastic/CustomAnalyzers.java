package com.ant.search.cerebro.service.index.elastic;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * @author sidhant.aggarwal
 * @since 07/07/2020
 */
@Service
public class CustomAnalyzers {
    static final String AUTOCOMPLETE_ANALYZER_NAME = "autocomplete_analyzer";

    private static final String AUTOCOMPLETE_TOKENIZER_NAME = "autocomplete_tokenizer";

    Map<String, Object> getCustomAnalyzers() {
        final Map<String, Object> analysisMap = new HashMap<>();
        analysisMap.put("analyzer", addCustomAnalyzerSettings());
        analysisMap.put("tokenizer", addCustomTokenizers());
        final Map<String, Object> settingsMap = new HashMap<>();
        settingsMap.put("analysis", analysisMap);
        return settingsMap;
    }

    private Map<String, Object> addCustomAnalyzerSettings() {
        final Map<String, Object> analyzerMap = new HashMap<>();
        addAutocompleteAnalyzer(analyzerMap);
        return analyzerMap;
    }

    private void addAutocompleteAnalyzer(final Map<String, Object> map) {
        final Map<String, Object> autocomplete = new HashMap<>();
        autocomplete.put("tokenizer", AUTOCOMPLETE_TOKENIZER_NAME);
        autocomplete.put("filter", Collections.singletonList("lowercase"));
        map.put(AUTOCOMPLETE_ANALYZER_NAME, autocomplete);
    }

    private Map<String, Object> addCustomTokenizers() {
        final Map<String, Object> tokenizerMap = new HashMap<>();
        addAutocompleteTokenizer(tokenizerMap);
        return tokenizerMap;
    }

    private void addAutocompleteTokenizer(final Map<String, Object> map) {
        final Map<String, Object> autocomplete = new HashMap<>();
        autocomplete.put("type", "edge_ngram");
        autocomplete.put("min_gram", 2);
        autocomplete.put("max_gram", 10);
        autocomplete.put("token_chars", Arrays.asList("letter", "digit"));
        map.put(AUTOCOMPLETE_TOKENIZER_NAME, autocomplete);
    }
}
