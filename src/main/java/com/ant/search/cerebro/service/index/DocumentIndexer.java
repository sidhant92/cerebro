package com.ant.search.cerebro.service.index;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.domain.index.FieldConfig;
import com.ant.search.cerebro.domain.index.IndexSettings;
import com.ant.search.cerebro.exception.Error;
import com.ant.search.cerebro.service.IndexSettingsService;
import com.ant.search.cerebro.service.datatype.PrimitiveDataTypeFactory;
import com.ant.search.cerebro.service.index.field.fixed.FixedFieldFactory;
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

    @Autowired
    private PrimitiveDataTypeFactory primitiveDataTypeFactory;

    @Autowired
    private FixedFieldFactory fixedFieldFactory;

    public Map<String, Object> cleanAndValidateDocument(final Map<String, Object> document, final String indexName) {
        final IndexSettings indexSettings = indexSettingsService.getCached(indexName).orElseThrow(Error.index_settings_not_found.getBuilder()::build);
        final Map<String, FieldConfig> flattenedFieldConfigMap = indexSettings.getStorageSettings().getFlattenedFieldConfigMap();
        return cleanAndValidateDocument(document, flattenedFieldConfigMap);
    }

    private Map<String, Object> cleanAndValidateDocument(final Map<String, Object> document, final Map<String, FieldConfig> flattenedFieldConfig) {
        final Map<String, Object> flattenedDocument = JsonUtils.flattenJson(document, objectMapper);
        filterExtraKeys(flattenedDocument, flattenedFieldConfig);
        fixedFieldFactory.getFixedFields().forEach(fixedField -> fixedField.isValid(document));
        if (!isDocumentValid(flattenedDocument, flattenedFieldConfig)) {
            log.error("Document is not Valid");
            throw Error.document_not_valid.getBuilder().build();
        }
        return JsonUtils.unFlattenJson(flattenedDocument, objectMapper);
    }

    private Boolean isDocumentValid(final Map<String, Object> document, final Map<String, FieldConfig> flattenedFieldConfig) {
        return document.keySet().stream().allMatch(key -> {
            if (fixedFieldFactory.getFixedFieldsNames().stream().anyMatch(key::startsWith)) {
                return true;
            }
            final FieldConfig fieldConfig = flattenedFieldConfig.get(key);
            boolean isValid;
            final Object value = document.get(key);
            switch (fieldConfig.getContainerDataType()) {
                case PRIMITIVE:
                    isValid = primitiveDataTypeFactory.getDataType(fieldConfig.getDataType()).isValid(value);
                    break;
                case LIST:
                    isValid = isObjectList(value) && ((List) value).stream().allMatch(
                            v -> primitiveDataTypeFactory.getDataType(fieldConfig.getDataType()).isValid(v));
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
        final Set<String> extraKeys = document.keySet().stream()
                                              .filter(key -> !flattenedFieldConfig.containsKey(key) && fixedFieldFactory.getFixedFieldsNames()
                                                                                                                         .stream()
                                                                                                                         .noneMatch(key::startsWith))
                                              .collect(Collectors.toSet());
        extraKeys.forEach(document::remove);
    }
}
