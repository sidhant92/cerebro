package com.ant.search.cerebro.service.parser.filter.canopy.domain;

import com.ant.search.cerebro.service.parser.filter.canopy.TreeNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author sidhant.aggarwal
 * @since 09/07/2020
 */
@AllArgsConstructor
@Getter
public class NumericRangeToken extends TreeNode {
    final String field;

    final Object fromValue;

    final Object toValue;

    final Boolean notEnabled;
}
