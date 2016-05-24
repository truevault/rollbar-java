package com.truevault.rollbar.payload.data.body;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;

/**
 * Represents the context around the code where the error occurred (lines before, 'pre', and after, 'post')
 */
public class CodeContext {
    private final List<String> pre;
    private final List<String> post;

    /**
     * @param pre  the lines of code before the one that triggered the error. List ownership passes to this object.
     * @param post the lines of code after the one that triggered the error. List ownership passes to this object.
     */
    public CodeContext(List<String> pre, List<String> post) {
        this.pre = pre == null ? null : Collections.unmodifiableList(pre);
        this.post = post == null ? null : Collections.unmodifiableList(post);
    }

    /**
     * @return the lines of code before the one that triggered the error
     */
    @JsonProperty("pre")
    public List<String> pre() {
        return pre;
    }

    /**
     * @return the lines of code after the one that triggered the error
     */
    @JsonProperty("post")
    public List<String> post() {
        return post;
    }
}
