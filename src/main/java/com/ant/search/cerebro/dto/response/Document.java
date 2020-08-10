package com.ant.search.cerebro.dto.response;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author sidhant.aggarwal
 * @since 10/08/2020
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Document {
    private Map<String, Object> source;

    private Map<String, Object> metaData;
}
