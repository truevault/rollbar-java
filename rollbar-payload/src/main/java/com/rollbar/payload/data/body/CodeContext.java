package com.rollbar.payload.data.body;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the context around the code where the error occurred (lines before, 'pre', and after, 'post')
 */
public class CodeContext {
    private final String[] pre;
    private final String[] post;

    /**
     * Constructor
     */
    public CodeContext() {
        this(null, null);
    }

    /**
     * Constructor
     * @param pre the lines of code before the one that triggered the error
     * @param post the lines of code after the one that triggered the error
     */
    public CodeContext(String[] pre, String[] post) {
        this.pre = pre == null ? null : pre.clone();
        this.post = post == null ? null : post.clone();
    }

    /**
     * @return the lines of code before the one that triggered the error
     */
    @JsonProperty("pre")
    public String[] pre() {
        return pre == null ? null : pre.clone();
    }

    /**
     * Set the lines of code before the one that triggered the error in a copy of this CodeContext
     * @param pre the new `pre` lines of code
     * @return a copy of this CodeContext with pre overridden
     */
    public CodeContext pre(String[] pre) {
        return new CodeContext(pre, post);
    }

    /**
     * @return the lines of code after the one that triggered the error
     */
    @JsonProperty("post")
    public String[] post() {
        return post == null ? null : post.clone();
    }

    /**
     * Set the lines of code after the one that triggered the error in a copy of this CodeContext
     * @param post the new `post` lines of code
     * @return a copy of this CodeContext with post overridden
     */
    public CodeContext post(String[] post) {
        return new CodeContext(pre, post);
    }

}
