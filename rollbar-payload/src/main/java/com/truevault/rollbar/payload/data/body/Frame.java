package com.truevault.rollbar.payload.data.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.truevault.rollbar.utilities.ArgumentNullException;
import com.truevault.rollbar.utilities.Validate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 * Represents a single frame from a stack trace
 */
public class Frame {
    private final String filename;
    private final Integer lineNumber;
    private final Integer columnNumber;
    private final String method;
    private final String code;
    private final CodeContext context;
    private final List<Object> args;
    private final LinkedHashMap<String, Object> keywordArgs;

    /**
     * Get an array of frames from an error
     *
     * @param error the error
     * @return the frames representing the error's stack trace
     * @throws ArgumentNullException if error is null
     */
    @Nonnull
    public static List<Frame> fromThrowable(Throwable error) throws ArgumentNullException {
        Validate.isNotNull(error, "error");
        StackTraceElement[] elements = error.getStackTrace();
        ArrayList<Frame> result = new ArrayList<>();
        for (StackTraceElement element : elements) {
            result.add(fromStackTraceElement(element));
        }
        Collections.reverse(result);
        return result;
    }

    /**
     * Get a Frame from a StackTraceElement
     *
     * @param stackTraceElement the StackTraceElement (a.k.a.: stack frame)
     * @return the Frame representing the StackTraceElement
     * @throws ArgumentNullException if stackTraceElement is null
     */
    public static Frame fromStackTraceElement(StackTraceElement stackTraceElement) throws ArgumentNullException {
        String filename = stackTraceElement.getClassName() + ".java";
        Integer lineNumber = stackTraceElement.getLineNumber();
        String method = stackTraceElement.getMethodName();

        return new Frame(filename, lineNumber, null, method, null, null, null, null);
    }

    /**
     * @param filename the name of the file in which the error occurred
     * @throws ArgumentNullException if filename is null
     */
    public Frame(String filename) throws ArgumentNullException {
        this(filename, null, null, null, null, null, null, null);
    }

    /**
     * Constructor
     *
     * @param filename     the name of the file in which the error occurred
     * @param lineNumber   the line number on which the error occurred
     * @param columnNumber the column number (if available in your language) on which the error occurred
     * @param method       the method in which the error occurred
     * @param code         the line of code that triggered the error
     * @param context      extra context around the line of code that triggered the error
     * @param args         the arguments to the method from the stack frame (if available in your language). List ownership passes to this object.
     * @param keywordArgs  the keyword arguments to the method from the stack frame (if available in your language)
     * @throws ArgumentNullException if filename is null
     */
    public Frame(String filename, Integer lineNumber, Integer columnNumber, String method, String code,
            CodeContext context, List<Object> args, Map<String, Object> keywordArgs) throws ArgumentNullException {
        Validate.isNotNullOrWhitespace(filename, "filename");
        this.filename = filename;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        this.method = method;
        this.code = code;
        this.context = context;
        this.args = args == null ? null : Collections.unmodifiableList(args);
        this.keywordArgs = keywordArgs == null ? null : new LinkedHashMap<>(keywordArgs);
    }

    /**
     * @return the name of the file in which the error occurred
     */
    @JsonProperty("filename")
    public String filename() {
        return filename;
    }

    /**
     * @return the line number on which the error occurred
     */
    @JsonProperty("lineno")
    public Integer lineNumber() {
        return lineNumber;
    }

    /**
     * @return the column number (if available in your language) on which the error occurred
     */
    @JsonProperty("colno")
    public Integer columnNumber() {
        return columnNumber;
    }

    /**
     * @return the method in which the error occurred
     */
    @JsonProperty("method")
    public String method() {
        return method;
    }

    /**
     * @return the line of code that triggered the error
     */
    @JsonProperty("code")
    public String code() {
        return code;
    }

    /**
     * @return extra context around the line of code that triggered the error
     */
    @JsonProperty("context")
    public CodeContext context() {
        return context;
    }

    /**
     * @return the arguments to the method from the stack frame (if available in your language)
     */
    @JsonProperty("args")
    public List<Object> args() {
        return args;
    }

    /**
     * @return the keyword arguments to the method from the stack frame (if available in your language)
     */
    @JsonProperty("kwargs")
    public Map<String, Object> keywordArgs() {
        return keywordArgs;
    }
}
