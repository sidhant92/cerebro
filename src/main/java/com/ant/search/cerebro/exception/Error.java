package com.ant.search.cerebro.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author sidhant.aggarwal
 * @since 05/07/2020
 */
@Getter
@AllArgsConstructor (access = AccessLevel.PRIVATE)
public enum Error {
    document_not_valid(AppException.builder().statusCode(400).header("Document not Valid").message("Document not Valid")),
    index_settings_not_found(AppException.builder().statusCode(404).header("Index Settings Not Found").message("Index Settings Not Found")),
    index_already_present(AppException.builder().statusCode(400).header("Index Already Present").message("Index Already Present")),
    document_not_found(AppException.builder().statusCode(404).header("Document Not Found").message("Document Not Found")),
    unknown_query_type(AppException.builder().statusCode(400).header("Unknown Query Type").message("Unknown Query Type")),
    unknown_error_occurred(AppException.builder().statusCode(500).header("Unknown Error Occurred").message("Unknown Error Occurred")),
    invalid_range_query(AppException.builder().statusCode(400).header("Invalid Range Query").message("Invalid Range Query")),
    invalid_operator(AppException.builder().statusCode(400).header("Invalid Operator").message("Invalid Operator")),
    invalid_filter_expression(AppException.builder().statusCode(400).header("Invalid Filter Expression").message("Invalid Filter Expression"));

    private AppException.AppExceptionBuilder builder;

    public AppException.AppExceptionBuilder getBuilder() {
        return builder.errorCode(this.name());
    }
}
