package com.ant.search.cerebro.domain.index;

import javax.validation.constraints.NotNull;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.ant.search.cerebro.constant.QueryStrategyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author sidhant.aggarwal
 * @since 09/07/2020
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBDocument
public class SearchSettings {
    @DynamoDBTyped (DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    @DynamoDBAttribute (attributeName = "query_strategy")
    @NotNull
    private QueryStrategyType queryStrategy;

    @DynamoDBTyped (DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    @DynamoDBAttribute (attributeName = "max_values_per_facet")
    private Integer maxValuesPerFacet = 100;
}
