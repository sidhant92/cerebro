package com.ant.search.cerebro.configuration;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sidhant.aggarwal
 * @since 04/07/2020
 */
@Configuration
public class ElasticConfig {
    @Value ("${elastic.host}")
    private String host;

    @Value ("${elastic.port}")
    private Integer port;

    @Value ("${elastic.scheme}")
    private String scheme;

    @Bean
    public RestHighLevelClient getElasticClient() {
        return new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, scheme)));
    }
}
