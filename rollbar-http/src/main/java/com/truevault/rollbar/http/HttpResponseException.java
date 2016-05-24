package com.truevault.rollbar.http;

/**
 * Represents an unsuccessful HTTP response.
 */
public class HttpResponseException extends Exception {

    private final int httpStatusCode;

    public HttpResponseException(int httpStatusCode, String message) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
