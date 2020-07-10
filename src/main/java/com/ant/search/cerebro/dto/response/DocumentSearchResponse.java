package com.ant.search.cerebro.dto.response;

import java.util.List;
import java.util.Map;
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
public class DocumentSearchResponse {
    private Long totalHits;

    private Boolean nextPage;

    private List<Map<String, Object>> documents;

    private Map<String, Map<String, Long>> facets;
}
