package com.ant.search.cerebro.constant;

import lombok.extern.slf4j.Slf4j;

/**
 * @author sidhant.aggarwal
 * @since 04/07/2020
 */
@Slf4j
public enum DataType {
    string() {
        public boolean isValid(Object value) {
            return value instanceof String;
        }
    },
    integer() {
        public boolean isValid(Object value) {
            return value instanceof Integer;
        }
    },
    bool() {
        public boolean isValid(Object value) {
            return value instanceof Boolean && ("true".equalsIgnoreCase(String.valueOf(value)) || "false".equalsIgnoreCase(String.valueOf(value)));
        }
    },
    decimal() {
        public boolean isValid(Object value) {
            return value instanceof Double;
        }
    };

    public abstract boolean isValid(final Object value);
}
