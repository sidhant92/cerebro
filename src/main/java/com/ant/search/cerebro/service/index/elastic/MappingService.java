package com.ant.search.cerebro.service.index.elastic;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.constant.DataType;
import com.ant.search.cerebro.domain.index.FieldConfig;
import com.ant.search.cerebro.domain.index.IndexSettings;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sidhant.aggarwal
 * @since 04/07/2020
 */
@Service
@Slf4j
public class MappingService {
    @Autowired
    private RestHighLevelClient elasticClient;

    public void putMapping(final IndexSettings indexSettings) {
        final Map<String, Object> properties = getFieldMappings(indexSettings.getStorageSettings().getFields());
        final PutMappingRequest request = new PutMappingRequest(indexSettings.getIndexName());
        request.source(properties);
        try {
            elasticClient.indices().putMapping(request, RequestOptions.DEFAULT);
        } catch (final Exception ex) {
            log.error("Error in creating mapping with message {}", ex.getMessage());
        }
    }

    private Map<String, Object> getFieldMappings(final List<FieldConfig> fields) {
        final Map<String, Object> mappingData = new HashMap<>();
        final Map<String, Object> fieldMappings = new HashMap<>();
        fields.forEach(fieldConfig -> {
            if (Optional.ofNullable(fieldConfig.getProperties()).orElse(Collections.emptyList()).isEmpty()) {
                final Map<String, Object> fieldLevelMapping = new HashMap<>(getFieldProperties(fieldConfig));
                fieldMappings.put(fieldConfig.getName(), fieldLevelMapping);
            } else {
                fieldMappings.put(fieldConfig.getName(), getFieldMappings(fieldConfig.getProperties()));
            }
        });
        mappingData.put("properties", fieldMappings);
        return mappingData;
    }

    private Map<String, Object> getFieldProperties(final FieldConfig fieldConfig) {
        final Map<String, Object> fieldProperties = new HashMap<>();
        fieldProperties.put("type", mapToElasticDataType(fieldConfig.getDataType(), fieldConfig.getTokenize()));
        return fieldProperties;
    }

    private String mapToElasticDataType(final DataType dataType, final Boolean tokenize) {
        switch (dataType) {
            case String:
                return tokenize ? "text" : "keyword";
            case Integer:
                return "integer";
            case Boolean:
                return "boolean";
            case Double:
                return "double";
            default:
                return "text";
        }
    }
}
