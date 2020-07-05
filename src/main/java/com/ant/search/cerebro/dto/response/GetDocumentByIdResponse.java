package com.ant.search.cerebro.dto.response;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author sidhant.aggarwal
 * @since 05/07/2020
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetDocumentByIdResponse {
    private Map<String, Object> document;
}
