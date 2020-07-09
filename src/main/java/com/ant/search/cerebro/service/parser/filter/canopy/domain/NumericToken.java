package com.ant.search.cerebro.service.parser.filter.canopy.domain;

import com.ant.search.cerebro.service.parser.filter.canopy.TreeNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author sidhant.aggarwal
 * @since 08/07/2020
 */
@AllArgsConstructor
@Getter
public class NumericToken extends TreeNode {
    final String field;

    final Object value;

    final String operator;

    final Boolean notEnabled;
}
