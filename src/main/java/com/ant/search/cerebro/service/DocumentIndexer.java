package com.ant.search.cerebro.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.domain.index.FieldConfig;
import com.ant.search.cerebro.domain.index.IndexSettings;
import com.ant.search.cerebro.exception.Error;
import com.ant.search.cerebro.util.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sidhant.aggarwal
 * @since 04/07/2020
 */
@Service
@Slf4j
public class DocumentIndexer {
    @Autowired
    private IndexSettingsService indexSettingsService;

    @Autowired
    private ObjectMapper objectMapper;

    public Map<String, Object> cleanAndValidateDocument(final Map<String, Object> document, final String indexName) {
        final IndexSettings indexSettings = indexSettingsService.get(indexName).orElseThrow(Error.index_settings_not_found.getBuilder()::build);
        final Map<String, FieldConfig> flattenedFieldConfigMap = getFlattenedFieldConfigs(indexSettings);
        return cleanAndValidateDocument(document, flattenedFieldConfigMap);
    }

    private Map<String, Object> cleanAndValidateDocument(final Map<String, Object> document, final Map<String, FieldConfig> flattenedFieldConfig) {
        final Map<String, Object> flattenedDocument = JsonUtils.flattenJson(document, objectMapper);
        filterExtraKeys(flattenedDocument, flattenedFieldConfig);
        if (!isDocumentValid(flattenedDocument, flattenedFieldConfig)) {
            log.error("Document is not Valid");
            throw Error.document_not_valid.getBuilder().build();
        }
        return JsonUtils.unFlattenJson(flattenedDocument, objectMapper);
    }

    private Boolean isDocumentValid(final Map<String, Object> document, final Map<String, FieldConfig> flattenedFieldConfig) {
        return document.keySet().stream().allMatch(key -> {
            final FieldConfig fieldConfig = flattenedFieldConfig.get(key);
            boolean isValid;
            final Object value = document.get(key);
            switch (fieldConfig.getContainerDataType()) {
                case primitive:
                    isValid = fieldConfig.getDataType().isValid(value);
                    break;
                case list:
                    isValid = isObjectList(value) && ((List) value).stream().allMatch(v -> fieldConfig.getDataType().isValid(v));
                    break;
                default:
                    isValid = false;
            }
            return isValid;
        });
    }

    private Boolean isObjectList(final Object value) {
        return value instanceof List;
    }

    private void filterExtraKeys(final Map<String, Object> document, final Map<String, FieldConfig> flattenedFieldConfig) {
        final Set<String> extraKeys = document.keySet().stream().filter(key -> !flattenedFieldConfig.containsKey(key)).collect(Collectors.toSet());
        extraKeys.forEach(document::remove);
    }

    public Map<String, FieldConfig> getFlattenedFieldConfigs(final IndexSettings indexSettings) {
        final Map<String, FieldConfig> fieldConfigMap = new HashMap<>();
        indexSettings.getStorageSettings().getFields().forEach(fieldConfig -> {
            if (Optional.ofNullable(fieldConfig.getProperties()).orElse(Collections.emptyList()).isEmpty()) {
                fieldConfigMap.put(fieldConfig.getName(), fieldConfig);
            } else {
                final String prefix = fieldConfig.getName();
                putNestedFieldConfigs(prefix, fieldConfig.getProperties(), fieldConfigMap);
            }
        });
        return fieldConfigMap;
    }

    private void putNestedFieldConfigs(final String prefix, final List<FieldConfig> nestedFields, final Map<String, FieldConfig> fieldConfigMap) {
        nestedFields.forEach(fieldConfig -> {
            final String newPrefix = prefix + "." + fieldConfig.getName();
            if (Optional.ofNullable(fieldConfig.getProperties()).orElse(Collections.emptyList()).isEmpty()) {
                fieldConfigMap.put(newPrefix, fieldConfig);
            } else {
                putNestedFieldConfigs(newPrefix, fieldConfig.getProperties(), fieldConfigMap);
            }
        });
    }
}
