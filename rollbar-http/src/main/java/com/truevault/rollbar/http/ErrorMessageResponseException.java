package com.truevault.rollbar.http;

/**
 * Represents an unsuccessful response that had an error message provided.
 */
public class ErrorMessageResponseException extends HttpResponseException {

    private final String errorMessage;

    public ErrorMessageResponseException(int httpStatusCode, String message) {
        super(httpStatusCode, "Error message from Rollbar: " + message);
        this.errorMessage = message;
    }

    /**
     * @return error message received in HTTP response
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}
