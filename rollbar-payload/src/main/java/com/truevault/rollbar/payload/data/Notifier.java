package com.truevault.rollbar.payload.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Information about this notifier, or one based off of this
 */
public class Notifier {
    public static final String defaultName = "rollbar";
    public static final String defaultVersion = Notifier.class.getPackage().getImplementationVersion();

    private final String name;
    private final String version;

    /**
     * Constructor, with "rollbar" and the maven assembly version for {@code name} and {@code version}
     */
    public Notifier() {
        this(defaultName, defaultVersion);
    }

    /**
     * Constructor
     * @param name the name of the notifier
     * @param version the version of the notifier
     */
    public Notifier(String name, String version) {
        this.name = name;
        this.version = version;
    }

    /**
     * @return the name
     */
    @JsonProperty("name")
    public String name() {
        return this.name;
    }

    /**
     * @return the version of the notifier
     */
    @JsonProperty("version")
    public String version() {
        return this.version;
    }

}
