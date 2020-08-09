package com.ant.search.cerebro.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author sidhant.aggarwal
 * @since 09/08/2020
 */
@NoArgsConstructor
@Getter
@Setter
public class CenterPoint {
    @NotNull
    private Double lat;

    @NotNull
    private Double lon;
}
