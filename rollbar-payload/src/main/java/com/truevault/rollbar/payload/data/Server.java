package com.truevault.rollbar.payload.data;

import com.truevault.rollbar.utilities.Extensible;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents the server object sent to Rollbar
 */
public class Server extends Extensible<Server> {
    public static final String HOST_KEY = "host";
    public static final String ROOT_KEY = "root";
    public static final String BRANCH_KEY = "branch";
    public static final String CODE_VERSION_KEY = "code_version";

    private Server(Map<String, Object> members) {
        super(members);
    }

    /**
     * @param host        the host
     * @param root        the file system root
     * @param branch      the current source control branch
     * @param codeVersion the current source control version (SHA, or name)
     */
    public Server(String host, String root, String branch, String codeVersion) {
        this(host, root, branch, codeVersion, null);
    }

    /**
     * @param host        the host
     * @param root        the file system root
     * @param branch      the current source control branch
     * @param codeVersion the current source control version (SHA, or name)
     * @param members     the extensible members
     */
    public Server(String host, String root, String branch, String codeVersion, Map<String, Object> members) {
        super(members);
        putKnown(HOST_KEY, host);
        putKnown(ROOT_KEY, root);
        putKnown(BRANCH_KEY, branch);
        putKnown(CODE_VERSION_KEY, codeVersion);
    }

    @Override
    public Server copy() {
        return new Server(getMembers());
    }

    /**
     * @return The host the code is running on
     */
    public String host() {
        return (String) get(HOST_KEY);
    }

    /**
     * @return the root
     */
    public String root() {
        return (String) get(ROOT_KEY);
    }

    /**
     * @return the branch
     */
    public String branch() {
        return (String) get(BRANCH_KEY);
    }

    /**
     * @return the code version
     */
    public String codeVersion() {
        return (String) get(CODE_VERSION_KEY);
    }

    @Override
    protected Set<String> getKnownMembers() {
        Set<String> result = new HashSet<>(4);
        Collections.addAll(result, HOST_KEY, ROOT_KEY, BRANCH_KEY, CODE_VERSION_KEY);
        return result;
    }
}
