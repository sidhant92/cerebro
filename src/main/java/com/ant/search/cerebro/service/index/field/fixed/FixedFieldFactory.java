package com.ant.search.cerebro.service.index.field.fixed;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author sidhant.aggarwal
 * @since 08/08/2020
 */
@Service
public class FixedFieldFactory {
    @Autowired
    private GeoLocation geoLocation;

    private final List<FixedField> fixedFields = new ArrayList<>();

    private final Set<String> fixedFieldNames = new HashSet<>();

    @PostConstruct
    public void register() {
        fixedFields.add(geoLocation);
        populateFixedFieldNames();
    }

    private void populateFixedFieldNames() {
        fixedFields.forEach(fixedField -> fixedFieldNames.add(fixedField.getKey()));
    }

    public List<FixedField> getFixedFields() {
        return fixedFields;
    }

    public Set<String> getFixedFieldsNames() {
        return fixedFieldNames;
    }
}
