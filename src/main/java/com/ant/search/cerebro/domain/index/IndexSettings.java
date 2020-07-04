package com.ant.search.cerebro.domain.index;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.ant.search.cerebro.constant.IndexProvider;
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
@DynamoDBTable (tableName = "index_settings")
public class IndexSettings {
    @DynamoDBHashKey (attributeName = "index_name")
    @DynamoDBTyped (DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    private String indexName;

    @DynamoDBTyped (DynamoDBMapperFieldModel.DynamoDBAttributeType.M)
    @DynamoDBAttribute (attributeName = "storage_settings")
    @NotNull (message = "Store Settings are mandatory")
    @Valid
    private StorageSettings storageSettings;

    @DynamoDBTyped (DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    @DynamoDBAttribute (attributeName = "index_provider")
    @NotNull (message = "Index Provider is mandatory")
    @Valid
    private IndexProvider indexProvider;
}
