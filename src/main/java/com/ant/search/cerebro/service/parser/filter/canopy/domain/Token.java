package com.ant.search.cerebro.service.parser.filter.canopy.domain;

import com.ant.search.cerebro.service.parser.filter.canopy.TreeNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author sidhant.aggarwal
 * @since 27/05/19
 */
@AllArgsConstructor
@Getter
public class Token extends TreeNode {
    final String field;

    final String value;

    final Boolean notEnabled;
}
