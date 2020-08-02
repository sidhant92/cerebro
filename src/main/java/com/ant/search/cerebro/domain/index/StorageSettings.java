package com.ant.search.cerebro.domain.index;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.ant.search.cerebro.constant.AnalyzerType;
import com.ant.search.cerebro.constant.DataType;
import com.ant.search.cerebro.domain.Mergeable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author sidhant.aggarwal
 * @since 04/07/2020
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBDocument
public class StorageSettings implements Mergeable<StorageSettings> {
    @DynamoDBAttribute (attributeName = "fields")
    @NotEmpty
    @Valid
    private List<FieldConfig> fields;

    @JsonIgnore
    private Map<String, FieldConfig> flattenedFieldConfigMap;

    public List<String> filterSearchableStringFields(final List<String> fields) {
        return fields.stream().filter(field -> this.flattenedFieldConfigMap.containsKey(field) && this.flattenedFieldConfigMap.get(field)
                                                                                                                              .getDataType() == DataType.STRING && this.flattenedFieldConfigMap
                .get(field).getSearchable()).collect(Collectors.toList());
    }

    public void setFlattenedFieldConfigMap() {
        final Map<String, FieldConfig> fieldConfigMap = new HashMap<>();
        this.fields.forEach(fieldConfig -> {
            if (Optional.ofNullable(fieldConfig.getProperties()).orElse(Collections.emptyList()).isEmpty()) {
                fieldConfigMap.put(fieldConfig.getName(), fieldConfig);
            } else {
                final String prefix = fieldConfig.getName();
                putNestedFieldConfigs(prefix, fieldConfig.getProperties(), fieldConfigMap);
            }
        });
        this.flattenedFieldConfigMap = fieldConfigMap;
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

    public AnalyzerType getAnalyzerType(final String field) {
        return this.flattenedFieldConfigMap.get(field).getAnalyzer();
    }

    @Override
    public void merge(final StorageSettings storageSettings) {
        final Map<String, FieldConfig> existingFieldConfigs = this.fields.stream().collect(Collectors.toMap(FieldConfig::getName, a -> a));
        storageSettings.getFields().forEach(fieldConfig -> {
            if (existingFieldConfigs.containsKey(fieldConfig.getName())) {
                existingFieldConfigs.get(fieldConfig.getName()).merge(fieldConfig);
            } else {
                this.fields.add(fieldConfig);
            }
        });
    }
}
