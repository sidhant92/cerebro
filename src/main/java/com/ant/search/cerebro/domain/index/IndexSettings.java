package com.ant.search.cerebro.domain.index;

import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperFieldModel;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTyped;
import com.ant.search.cerebro.constant.IndexProvider;
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
@DynamoDBTable (tableName = "index_settings")
public class IndexSettings implements Mergeable<IndexSettings> {
    @DynamoDBHashKey (attributeName = "index_name")
    @DynamoDBTyped (DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    private String indexName;

    @DynamoDBTyped (DynamoDBMapperFieldModel.DynamoDBAttributeType.M)
    @DynamoDBAttribute (attributeName = "storage_settings")
    @NotNull (message = "Store Settings are mandatory")
    @Valid
    private StorageSettings storageSettings;

    @DynamoDBTyped (DynamoDBMapperFieldModel.DynamoDBAttributeType.M)
    @DynamoDBAttribute (attributeName = "search_settings")
    @NotNull (message = "Search Settings are mandatory")
    @Valid
    private SearchSettings searchSettings;

    @DynamoDBTyped (DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    @DynamoDBAttribute (attributeName = "index_provider")
    @NotNull (message = "Index Provider is mandatory")
    @Valid
    private IndexProvider indexProvider;

    @DynamoDBTyped (DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    @DynamoDBAttribute (attributeName = "no_of_replicas")
    private Integer noOfReplicas = 1;

    @DynamoDBTyped (DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    @DynamoDBAttribute (attributeName = "no_of_shards")
    private Integer noOfShards = 1;

    @DynamoDBTyped (DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    private Long createdAt;

    @DynamoDBTyped (DynamoDBMapperFieldModel.DynamoDBAttributeType.N)
    private Long updatedAt;

    public void prePersist() {
        final long currentTime = System.currentTimeMillis();
        this.createdAt = currentTime;
        this.updatedAt = currentTime;
        this.getStorageSettings().setFlattenedFieldConfigMap();
    }

    public void preUpdate() {
        this.updatedAt = System.currentTimeMillis();
        this.getStorageSettings().setFlattenedFieldConfigMap();
    }

    public void merge(final IndexSettings indexSettings) {
        if (!Objects.isNull(indexSettings.getStorageSettings())) {
            this.storageSettings.merge(indexSettings.getStorageSettings());
        }
        if (!Objects.isNull(indexSettings.getSearchSettings())) {
            this.searchSettings = indexSettings.getSearchSettings();
        }
    }
}
