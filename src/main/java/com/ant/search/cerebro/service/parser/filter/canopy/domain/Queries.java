package com.ant.search.cerebro.service.parser.filter.canopy.domain;

import java.util.ArrayList;
import java.util.List;
import com.ant.search.cerebro.constant.QueryClause;
import com.ant.search.cerebro.service.parser.filter.canopy.TreeNode;
import lombok.Getter;

/**
 * @author sidhant.aggarwal
 * @since 27/05/19
 */
@Getter
public class Queries extends TreeNode {
    private List<TreeNode> orQueries;

    private List<TreeNode> andQueries;

    public Queries() {
        orQueries = new ArrayList<>();
        andQueries = new ArrayList<>();
    }

    public void addClause(final TreeNode query, final QueryClause clause) {
        if (clause == QueryClause.must) {
            andQueries.add(query);
        } else {
            orQueries.add(query);
        }
    }
}
