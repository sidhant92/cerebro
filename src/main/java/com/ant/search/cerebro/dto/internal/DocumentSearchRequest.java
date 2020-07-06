package com.ant.search.cerebro.dto.internal;

import com.ant.search.cerebro.domain.search.query.Query;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author sidhant.aggarwal
 * @since 06/07/2020
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DocumentSearchRequest {
    private Query query;

    private String indexName;

    private Long offset;

    private Long limit;
}