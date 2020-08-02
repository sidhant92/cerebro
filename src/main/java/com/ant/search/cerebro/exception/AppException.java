package com.ant.search.cerebro.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author sidhant.aggarwal
 * @since 05/07/2020
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class AppException extends WebException {
    private String errorCode;

    private Integer statusCode;

    private String message;

    private String header;

    private Map<String, Object> information = new HashMap<>();

    @Builder
    public AppException(final String errorCode, final Integer statusCode, final String message, final String header,
            final Map<String, Object> information) {
        super(message);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
        this.message = message;
        this.header = header;
        this.information = information;
    }

    public static AppExceptionBuilder newBuilder(AppExceptionBuilder builder) {
        return AppException.builder().statusCode(builder.statusCode).errorCode(builder.errorCode).header(builder.header)
                           .information(builder.information).message(builder.message);
    }

    public static class AppExceptionBuilder {
        public AppExceptionBuilder information(Map<String, Object> info) {
            this.information = info;
            return this;
        }

        public AppExceptionBuilder information(String key, Object value) {
            if (this.information == null || this.information.isEmpty()) {
                this.information = new HashMap<>();
            }
            this.information.put(key, value);
            return this;
        }
    }
}
