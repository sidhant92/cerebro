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
public class BoundingBox {
    @NotNull
    private Double topLeftLat;

    @NotNull
    private Double topLeftLon;

    @NotNull
    private Double bottomRightLat;

    @NotNull
    private Double bottomRightLon;
}
