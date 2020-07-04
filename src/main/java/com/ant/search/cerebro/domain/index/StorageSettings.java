package com.ant.search.cerebro.domain.index;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
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
public class StorageSettings {
    @DynamoDBAttribute (attributeName = "fields")
    @NotEmpty
    @Valid
    private List<FieldConfig> fields;
}
