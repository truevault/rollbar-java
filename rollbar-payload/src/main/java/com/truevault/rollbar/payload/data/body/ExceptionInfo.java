package com.truevault.rollbar.payload.data.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.truevault.rollbar.utilities.ArgumentNullException;
import com.truevault.rollbar.utilities.Validate;

/**
 * Represents *non-stacktrace* information about an exception, like class, description, and message.
 */
public class ExceptionInfo {
    private final String className;
    private final String message;
    private final String description;

    /**
     * Create an exception info from a throwable.
     *
     * @param error the throwable
     * @return an exception info with information gathered from the error
     * @throws ArgumentNullException if the error is null
     */
    public static ExceptionInfo fromThrowable(Throwable error) throws ArgumentNullException {
        return fromThrowable(error, null);
    }

    /**
     * Create an exception info from an error and a (human readable) description of the error
     *
     * @param error       the error
     * @param description the human readable description of the error
     * @return the ExceptionInfo built from the error and the description
     * @throws ArgumentNullException if the error is null
     */
    public static ExceptionInfo fromThrowable(Throwable error, String description) throws ArgumentNullException {
        Validate.isNotNull(error, "error");
        String className = error.getClass().getSimpleName();
        String message = error.getMessage();
        return new ExceptionInfo(className, message, description);
    }

    /**
     * Constructor
     *
     * @param className the name of the exception class
     * @throws ArgumentNullException if the name is null or whitespace
     */
    public ExceptionInfo(String className) throws ArgumentNullException {
        this(className, null, null);
    }

    /**
     * Constructor
     *
     * @param className   the name of the exception class
     * @param message     the exception message
     * @param description a human readable description of the exception
     * @throws ArgumentNullException if className is null or whitespace
     */
    public ExceptionInfo(String className, String message, String description) throws ArgumentNullException {
        Validate.isNotNullOrWhitespace(className, "className");
        this.className = className;
        this.message = message;
        this.description = description;
    }

    /**
     * @return the name of the exception class
     */
    @JsonProperty("class")
    public String className() {
        return this.className;
    }

    /**
     * @return the exception message
     */
    @JsonProperty("message")
    public String message() {
        return this.message;
    }

    /**
     * @return a human readable description of the exception
     */
    @JsonProperty("description")
    public String description() {
        return this.description;
    }
}
