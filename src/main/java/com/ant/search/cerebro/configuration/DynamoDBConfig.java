package com.ant.search.cerebro.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

/**
 * @author sidhant.aggarwal
 * @since 04/07/2020
 */
@Configuration
public class DynamoDBConfig {
    @Value ("${aws.accessKey}")
    private String accessKey;

    @Value ("${aws.secret}")
    private String secret;

    @Value ("${aws.region}")
    private String region;

    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withCredentials(
                new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secret))).withRegion(Regions.fromName(region)).build();
        return new DynamoDBMapper(client, DynamoDBMapperConfig.DEFAULT);
    }
}
