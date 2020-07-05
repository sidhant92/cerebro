package com.ant.search.cerebro.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author sidhant.aggarwal
 * @since 05/07/2020
 */
@NoArgsConstructor
@Getter
@ToString
public class WebException extends RuntimeException {
    public WebException(final String message) {
        super(message);
    }

    public WebException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public WebException(final Throwable cause) {
        super(cause);
    }

    public WebException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
