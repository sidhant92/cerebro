package com.ant.search.cerebro.domain.index;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.ant.search.cerebro.constant.AnalyzerType;
import com.ant.search.cerebro.constant.ContainerType;
import com.ant.search.cerebro.constant.DataType;
import com.ant.search.cerebro.domain.Mergeable;
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
public class FieldConfig implements Mergeable<FieldConfig> {
    @DynamoDBAttribute (attributeName = "name")
    @DynamoDBTyped (DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    @NotBlank
    private String name;

    @DynamoDBAttribute (attributeName = "data_type")
    @DynamoDBTyped (DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    @NotNull
    private DataType dataType;

    @DynamoDBAttribute (attributeName = "container_type")
    @DynamoDBTyped (DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    @NotNull
    private ContainerType containerDataType;

    @DynamoDBAttribute (attributeName = "analyzer")
    @DynamoDBTyped (DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    @NotNull
    private AnalyzerType analyzer = AnalyzerType.STANDARD;

    @DynamoDBAttribute (attributeName = "tokenize")
    @NotNull
    private Boolean tokenize;

    @DynamoDBAttribute (attributeName = "searchable")
    @NotNull
    private Boolean searchable;

    @DynamoDBAttribute (attributeName = "prefix_search_enabled")
    @NotNull
    private Boolean prefixSearchEnabled = true;

    @DynamoDBAttribute (attributeName = "facet_required")
    @NotNull
    private Boolean facetRequired = false;

    @DynamoDBAttribute (attributeName = "properties")
    @Valid
    private List<FieldConfig> properties;

    @Override
    public void merge(final FieldConfig fieldConfig) {
        if (!Objects.isNull(fieldConfig.getAnalyzer())) {
            this.analyzer = fieldConfig.getAnalyzer();
        }
        if (!Objects.isNull(fieldConfig.getTokenize())) {
            this.tokenize = fieldConfig.getTokenize();
        }
        if (!Objects.isNull(fieldConfig.getSearchable())) {
            this.searchable = fieldConfig.getSearchable();
        }
        if (!Objects.isNull(fieldConfig.getPrefixSearchEnabled())) {
            this.prefixSearchEnabled = fieldConfig.getPrefixSearchEnabled();
        }
        if (!Objects.isNull(fieldConfig.getFacetRequired())) {
            this.facetRequired = fieldConfig.getFacetRequired();
        }
        if (!Objects.isNull(fieldConfig.getProperties())) {
            final Map<String, FieldConfig> existingProperties = this.getProperties().stream().collect(Collectors.toMap(FieldConfig::getName, a -> a));
            fieldConfig.getProperties().forEach(property -> {
                if (existingProperties.containsKey(property.getName())) {
                    existingProperties.get(property.getName()).merge(property);
                } else {
                    this.properties.add(property);
                }
            });
        }
    }
}
