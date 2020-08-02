package com.ant.search.cerebro.service.datatype;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ant.search.cerebro.constant.DataType;

/**
 * @author sidhant.aggarwal
 * @since 08/07/2020
 */
@Service
public class PrimitiveDataTypeFactory {
    @Autowired
    private BoolDataType boolDataType;

    @Autowired
    private DecimalDataType decimalDataType;

    @Autowired
    private IntegerDataType integerDataType;

    @Autowired
    private StringDataType stringDataType;

    private final Map<DataType, PrimitiveAbstractType> dataTypeFactoryMap = new HashMap<>();

    @PostConstruct
    public void register() {
        dataTypeFactoryMap.put(DataType.BOOL, boolDataType);
        dataTypeFactoryMap.put(DataType.DECIMAL, decimalDataType);
        dataTypeFactoryMap.put(DataType.INTEGER, integerDataType);
        dataTypeFactoryMap.put(DataType.STRING, stringDataType);
    }

    public PrimitiveAbstractType getDataType(final DataType dataType) {
        return dataTypeFactoryMap.get(dataType);
    }
}
