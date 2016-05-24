package com.truevault.rollbar.payload.data.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.truevault.rollbar.utilities.ArgumentNullException;
import com.truevault.rollbar.utilities.Validate;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * Represent a Stack Trace to send to Rollbar
 */
public class Trace implements BodyContents {
    private final List<Frame> frames;

    private final ExceptionInfo exception;

    /**
     * Create a stack trace from a throwable
     *
     * @param error       the Throwable to create a stack trace from
     * @param description human readable description of the error
     * @return the Trace representing the Throwable
     * @throws ArgumentNullException if error is null
     */
    public static Trace fromThrowable(Throwable error, String description) throws ArgumentNullException {
        Validate.isNotNull(error, "error");

        List<Frame> frames = Frame.fromThrowable(error);
        ExceptionInfo exceptionInfo = ExceptionInfo.fromThrowable(error, description);

        return new Trace(frames, exceptionInfo);
    }

    /**
     * Create a stack trace from a throwable
     *
     * @param error the Throwable to create a stack trace from
     * @return the Trace representing the Throwable
     * @throws ArgumentNullException if error is null
     */
    public static Trace fromThrowable(Throwable error) throws ArgumentNullException {
        return fromThrowable(error, null);
    }

    /**
     * @param frames    not nullable, frames making up the exception. List ownership passes to this object.
     * @param exception not nullable, info about the exception
     * @throws ArgumentNullException if either argument is null
     */
    public Trace(@Nonnull List<Frame> frames, ExceptionInfo exception) throws ArgumentNullException {
        Validate.isNotNull(frames, "frames");
        this.frames = Collections.unmodifiableList(frames);
        Validate.isNotNull(exception, "exception");
        this.exception = exception;
    }

    /**
     * @return a copy of the frames
     */
    @JsonProperty("frames")
    public List<Frame> frames() {
        return this.frames;
    }

    /**
     * @return the exception info
     */
    @JsonProperty("exception")
    public ExceptionInfo exception() {
        return this.exception;
    }
}
