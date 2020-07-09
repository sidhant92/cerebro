package com.ant.search.cerebro.service.parser.filter.canopy;

import java.util.List;
import com.ant.search.cerebro.constant.QueryClause;
import com.ant.search.cerebro.service.parser.filter.canopy.domain.NumericRangeToken;
import com.ant.search.cerebro.service.parser.filter.canopy.domain.NumericToken;
import com.ant.search.cerebro.service.parser.filter.canopy.domain.Queries;
import com.ant.search.cerebro.service.parser.filter.canopy.domain.Token;

/**
 * @author sidhant.aggarwal
 * @since 27/05/19
 */
public class ActionsImpl implements Actions {
    public Token make_string_token(String input, int start, int end, List<TreeNode> elements) {
        return new Token(elements.get(2).text, elements.get(6).text, elements.get(0).text.equals("NOT"));
    }

    @Override
    public TreeNode make_numeric_token(String input, int start, int end, List<TreeNode> elements) {
        final String field = elements.get(2).text;
        final String operator = elements.get(4).text;
        final String stringValue = elements.get(6).text;
        final Object value = stringValue.indexOf('.') == -1 ? Integer.parseInt(stringValue) : Double.parseDouble(stringValue);
        return new NumericToken(field, value, operator, elements.get(0).text.equals("NOT"));
    }

    @Override
    public TreeNode make_numeric_range_token(String input, int start, int end, List<TreeNode> elements) {
        final String field = elements.get(2).text;
        final String fromStringValue = elements.get(6).text;
        final String toStringValue = elements.get(10).text;
        final Object fromValue = fromStringValue.indexOf('.') == -1 ? Integer.parseInt(fromStringValue) : Double.parseDouble(fromStringValue);
        final Object toValue = toStringValue.indexOf('.') == -1 ? Integer.parseInt(toStringValue) : Double.parseDouble(toStringValue);
        return new NumericRangeToken(field, fromValue, toValue, elements.get(0).text.equals("NOT"));
    }

    public TreeNode make_primary(String input, int start, int end, List<TreeNode> elements) {
        return elements.get(1);
    }

    public TreeNode make_logical_and(String input, int start, int end, List<TreeNode> elements) {
        Queries queries = new Queries();
        for (TreeNode node : elements.get(1)) {
            queries.addClause(node.get(Label.primary), QueryClause.must);
        }
        if (queries.getAndQueries().size() == 0) {
            return elements.get(0);
        } else {
            queries.addClause(elements.get(0), QueryClause.must);
            return queries;
        }
    }

    public TreeNode make_logical_or(String input, int start, int end, List<TreeNode> elements) {
        Queries queries = new Queries();
        if (elements.get(0) instanceof Queries) {
            queries = (Queries) elements.get(0);
        } else {
            queries.addClause(elements.get(0), QueryClause.should);
        }
        for (TreeNode node : elements.get(1)) {
            queries.addClause(node.get(Label.logical_and), QueryClause.should);
        }
        if (queries.getAndQueries().size() == 0 && queries.getOrQueries().size() == 0) {
            return elements.get(0);
        } else {
            return queries;
        }
    }
}
