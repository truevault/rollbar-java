package com.truevault.rollbar.payload.data.body;

import com.fasterxml.jackson.annotation.JsonValue;
import com.truevault.rollbar.utilities.ArgumentNullException;
import com.truevault.rollbar.utilities.InvalidLengthException;
import com.truevault.rollbar.utilities.Validate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a chain of errors (typically from Exceptions with {@link Exception#getCause()} returning some value)
 */
public class TraceChain implements BodyContents {
    private final List<Trace> traces;

    /**
     * Generate a TraceChain from a throwable with multiple causes
     *
     * @param throwable the throwable to record
     * @return the trace chain representing the Throwable
     * @throws ArgumentNullException if throwable is null
     */
    public static TraceChain fromThrowable(Throwable throwable) throws ArgumentNullException {
        return fromThrowable(throwable, null);
    }

    /**
     * Generate a TraceChain from a throwable with multiple causes
     *
     * @param throwable   the throwable to record
     * @param description a human readable description of the first throwable in the chain
     * @return the trace chain representing the Throwable
     * @throws ArgumentNullException if throwable is null
     */
    public static TraceChain fromThrowable(Throwable throwable, String description) throws ArgumentNullException {
        Validate.isNotNull(throwable, "throwable");
        ArrayList<Trace> chain = new ArrayList<>();
        do {
            chain.add(Trace.fromThrowable(throwable, description));
            description = null;
            throwable = throwable.getCause();
        } while (throwable != null);
        return new TraceChain(chain);
    }

    /**
     * @param traces the traces making up this trace chain. List ownership passes to this object.
     * @throws ArgumentNullException  if traces are null
     * @throws InvalidLengthException if there are no traces in the array
     */
    public TraceChain(List<Trace> traces) throws ArgumentNullException, InvalidLengthException {
        Validate.isNotNull(traces, "traces");
        Validate.minLength(traces, 1, "traces");
        this.traces = Collections.unmodifiableList(traces);
    }

    public List<Trace> traces() {
        return traces;
    }

    @JsonValue
    public List<Trace> asJson() {
        return traces();
    }
}
