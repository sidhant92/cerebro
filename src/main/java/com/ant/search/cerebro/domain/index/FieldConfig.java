package com.ant.search.cerebro.domain.index;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.ant.search.cerebro.constant.ContainerType;
import com.ant.search.cerebro.constant.DataType;
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
public class FieldConfig {
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

    @DynamoDBAttribute (attributeName = "tokenize")
    @DynamoDBTyped (DynamoDBMapperFieldModel.DynamoDBAttributeType.B)
    @NotNull
    private Boolean tokenize;

    @DynamoDBAttribute (attributeName = "properties")
    @Valid
    private List<FieldConfig> properties;
}
