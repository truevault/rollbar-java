package com.truevault.rollbar.payload.data.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.truevault.rollbar.utilities.ArgumentNullException;
import com.truevault.rollbar.utilities.Validate;

/**
 * Represents *non-stacktrace* information about an exception, like class, description, and message.
 */
public class ExceptionInfo {
    /**
     * Create an exception info from a throwable.
     * @param error the throwable
     * @throws ArgumentNullException if the error is null
     * @return an exception info with information gathered from the error
     */
    public static ExceptionInfo fromThrowable(Throwable error) throws ArgumentNullException {
        return fromThrowable(error, null);
    }

    /**
     * Create an exception info from an error and a (human readable) description of the error
     * @param error the error
     * @param description the human readable description of the error
     * @throws ArgumentNullException if the error is null
     * @return the ExceptionInfo built from the error and the description
     */
    public static ExceptionInfo fromThrowable(Throwable error, String description) throws ArgumentNullException {
        Validate.isNotNull(error, "error");
        String className = error.getClass().getSimpleName();
        String message = error.getMessage();
        return new ExceptionInfo(className, message, description);
    }

    private final String className;
    private final String message;
    private final String description;

    /**
     * Constructor
     * @param className the name of the exception class
     * @throws ArgumentNullException if the name is null or whitespace
     */
    public ExceptionInfo(String className) throws ArgumentNullException {
        this(className, null, null);
    }

    /**
     * Constructor
     * @param className the name of the exception class
     * @param message the exception message
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
     * Set the className on a copy of this ExceptionInfo
     * @param className the new className
     * @return a copy of this ExceptionInfo with className overridden
     * @throws ArgumentNullException if className is null or whitespace
     */
    public ExceptionInfo className(String className) throws ArgumentNullException {
        return new ExceptionInfo(className, message, description);
    }

    /**
     * @return the exception message
     */
    @JsonProperty("message")
    public String message() {
        return this.message;
    }

    /**
     * Set the message on a copy of this ExceptionInfo
     * @param message the new message
     * @return a copy of this ExceptionInfo with message overridden
     */
    public ExceptionInfo message(String message) {
        return new ExceptionInfo(className, message, description);
    }

    /**
     * @return a human readable description of the exception
     */
    @JsonProperty("description")
    public String description() {
        return this.description;
    }

    /**
     * Set the description on a copy of this ExceptionInfo
     * @param description the new description
     * @return a copy of this ExceptionInfo with description overriden
     */
    public ExceptionInfo description(String description) {
        return new ExceptionInfo(className, message, description);
    }

}
