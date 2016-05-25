package com.truevault.rollbar.payload.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.truevault.rollbar.payload.data.body.Body;
import com.truevault.rollbar.utilities.ArgumentNullException;
import com.truevault.rollbar.utilities.InvalidLengthException;
import com.truevault.rollbar.utilities.Validate;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * Represents the actual data being posted to Rollbar
 */
public class Data {
    @Nonnull
    private final String environment;
    @Nonnull
    private final Body body;
    private final Level level;
    private final Instant timestamp;
    private final String codeVersion;
    private final String platform;
    private final String language;
    private final String framework;
    private final String context;
    private final Request request;
    private final Person person;
    private final Server server;
    private final LinkedHashMap<String, Object> custom;
    private final String fingerprint;
    private final String title;
    private final UUID uuid;
    private final Notifier notifier;

    /**
     * @throws ArgumentNullException  if environment or body is null
     * @throws InvalidLengthException if environment or title is over 255 characters, or uuid is over 32 characters
     */
    private Data(@Nonnull String environment, @Nonnull Body body, Level level, Instant timestamp, String codeVersion,
            String platform, String language, String framework, String context, Request request, Person person,
            Server server, Map<String, Object> custom, String fingerprint, String title, UUID uuid,
            Notifier notifier) throws ArgumentNullException, InvalidLengthException {
        Validate.isNotNullOrWhitespace(environment, "environment");
        Validate.maxLength(environment, 255, "environment");
        Validate.isNotNull(body, "body");
        if (title != null) {
            Validate.maxLength(title, 255, "title");
        }
        this.environment = environment;
        this.body = body;
        this.level = level;
        this.timestamp = timestamp;
        this.codeVersion = codeVersion;
        this.platform = platform;
        this.language = language;
        this.framework = framework;
        this.context = context;
        this.request = request;
        this.person = person;
        this.server = server;
        this.custom = custom == null ? null : new LinkedHashMap<>(custom);
        this.fingerprint = fingerprint;
        this.title = title;
        this.uuid = uuid;
        this.notifier = notifier;
    }

    public Data.Builder toBuilder() {
        return new Builder(body, environment)
                .level(level)
                .timestamp(timestamp)
                .codeVersion(codeVersion)
                .platform(platform)
                .language(language)
                .framework(framework)
                .context(context)
                .request(request)
                .person(person)
                .server(server)
                .custom(custom)
                .fingerprint(fingerprint)
                .title(title)
                .uuid(uuid)
                .notifier(notifier);
    }

    /**
     * @return string representing the current environment (e.g.: production, debug, test)
     */
    @JsonProperty("environment")
    public String environment() {
        return this.environment;
    }

    /**
     * @return not nullable, the actual data being sent to rollbar (not metadata, about the request, server, etc.)
     */
    @JsonProperty("body")
    @Nonnull
    public Body body() {
        return this.body;
    }

    /**
     * @return the rollbar error level
     */
    @JsonProperty("level")
    public Level level() {
        return this.level;
    }

    /**
     * @return the moment the bug happened, visible in ui as client_timestamp
     */
    @JsonProperty("timestamp")
    public Instant timestamp() {
        return timestamp;
    }

    /**
     * @return the currently running version of the code
     */
    @JsonProperty("code_version")
    public String codeVersion() {
        return this.codeVersion;
    }

    /**
     * @return the platform running (most likely JVM and a version)
     */
    @JsonProperty("platform")
    public String platform() {
        return this.platform;
    }

    /**
     * @return the language running (most likely java, but any JVM language might be here)
     */
    @JsonProperty("language")
    public String language() {
        return this.language;
    }

    /**
     * @return the framework being run (e.g. Play, Spring, etc)
     */
    @JsonProperty("framework")
    public String framework() {
        return this.framework;
    }

    /**
     * @return custom identifier to help find where the error came from, Controller class name, for instance.
     */
    @JsonProperty("context")
    public String context() {
        return this.context;
    }

    /**
     * @return data about the Http Request that caused this, if applicable
     */
    @JsonProperty("request")
    public Request request() {
        return this.request;
    }

    /**
     * @return data about the user that experienced the error, if possible
     */
    @JsonProperty("person")
    public Person person() {
        return this.person;
    }

    /**
     * @return data about the machine on which the error occurred
     */
    @JsonProperty("server")
    public Server server() {
        return this.server;
    }

    /**
     * @return custom data that will aid in debugging the error
     */
    @JsonProperty("custom")
    public Map<String, Object> custom() {
        return custom == null ? null : new LinkedHashMap<>(this.custom);
    }

    /**
     * @return override the default and custom grouping with a string, if over 255 characters will be hashed
     */
    @JsonProperty("fingerprint")
    public String fingerprint() {
        return this.fingerprint;
    }

    /**
     * @return the title, max length 255 characters, overrides the default and custom ones set by rollbar
     */
    @JsonProperty("title")
    public String title() {
        return this.title;
    }

    /**
     * @return override the error UUID, unique to each project, used to deduplicate occurrences
     */
    @JsonProperty("uuid")
    public UUID uuid() {
        return uuid;
    }

    /**
     * @return information about this notifier, esp. if creating a framework specific notifier
     */
    @JsonProperty("notifier")
    public Notifier notifier() {
        return this.notifier;
    }

    @NotThreadSafe
    public static class Builder {
        private String environment;
        private Body body;
        private Level level = null;
        private Instant timestamp;
        private String codeVersion = null;
        private String platform = null;
        private String language = null;
        private String framework = null;
        private String context = null;
        private Request request = null;
        private Person person = null;
        private Server server = null;
        private Map<String, Object> custom = null;
        private String fingerprint = null;
        private String title = null;
        private Notifier notifier = null;
        private UUID uuid;

        /**
         * Create an empty Builder. Ensure that body and environment are set before calling build().
         */
        public Builder() {
        }

        /**
         * Create a Builder with body and environment set (the two required fields).
         *
         * Unless you are explicitly trying to create an incomplete Builder, you should use this constructor to ensure
         * you have the required fields.
         */
        public Builder(@Nonnull Body body, @Nonnull String environment) {
            this.body = body;
            this.environment = environment;
        }

        /**
         * @param environment not nullable, string representing the current environment (e.g.: production, debug, test)
         * @return this
         */
        public Builder environment(@Nonnull String environment) {
            this.environment = environment;
            return this;
        }

        /**
         * @param body not nullable, the actual data being sent to rollbar (not metadata, about the request, server,
         *             etc.)
         * @return this
         */
        public Builder body(@Nonnull Body body) {
            this.body = body;
            return this;
        }

        /**
         * @param level the rollbar error level
         * @return this
         */
        public Builder level(Level level) {
            this.level = level;
            return this;
        }

        /**
         * @param timestamp the moment the bug happened, visible in ui as client_timestamp
         * @return this
         */
        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /**
         * @param codeVersion the currently running version of the code
         * @return this
         */
        public Builder codeVersion(String codeVersion) {
            this.codeVersion = codeVersion;
            return this;
        }

        /**
         * @param platform the platform running (most likely JVM and a version)
         * @return this
         */
        public Builder platform(String platform) {
            this.platform = platform;
            return this;
        }

        /**
         * @param language the language running (most likely java, but any JVM language might be here)
         * @return this
         */
        public Builder language(String language) {
            this.language = language;
            return this;
        }

        /**
         * @param framework the framework being run (e.g. Play, Spring, etc)
         * @return this
         */
        public Builder framework(String framework) {
            this.framework = framework;
            return this;
        }

        /**
         * @param context custom identifier to help find where the error came from, Controller class name, for
         *                instance.
         * @return this
         */
        public Builder context(String context) {
            this.context = context;
            return this;
        }

        /**
         * @param request data about the Http Request that caused this, if applicable
         * @return this
         */
        public Builder request(Request request) {
            this.request = request;
            return this;
        }

        /**
         * @param person data about the user that experienced the error, if possible
         * @return this
         */
        public Builder person(Person person) {
            this.person = person;
            return this;
        }

        /**
         * @param server data about the machine on which the error occurred
         * @return this
         */
        public Builder server(Server server) {
            this.server = server;
            return this;
        }

        /**
         * @param custom custom data that will aid in debugging the error
         * @return this
         */
        public Builder custom(Map<String, Object> custom) {
            this.custom = custom;
            return this;
        }

        /**
         * @param fingerprint override the default and custom grouping with a string, if over 255 characters will be
         *                    hashed
         * @return this
         */
        public Builder fingerprint(String fingerprint) {
            this.fingerprint = fingerprint;
            return this;
        }

        /**
         * @param title the title, max length 255 characters, overrides the default and custom ones set by rollbar
         * @return this
         */
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * @param notifier information about this notifier, esp. if creating a framework specific notifier
         * @return this
         */
        public Builder notifier(Notifier notifier) {
            this.notifier = notifier;
            return this;
        }

        /**
         * @param uuid override the error UUID, unique to each project, used to deduplicate occurrences
         * @return this
         */
        public Builder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Data build() {
            return new Data(environment, body, level, timestamp, codeVersion, platform, language, framework, context,
                    request, person, server, custom, fingerprint, title, uuid, notifier);
        }

        public String getEnvironment() {
            return environment;
        }

        public Body getBody() {
            return body;
        }

        public Level getLevel() {
            return level;
        }

        public Instant getTimestamp() {
            return timestamp;
        }

        public String getCodeVersion() {
            return codeVersion;
        }

        public String getPlatform() {
            return platform;
        }

        public String getLanguage() {
            return language;
        }

        public String getFramework() {
            return framework;
        }

        public String getContext() {
            return context;
        }

        public Request getRequest() {
            return request;
        }

        public Person getPerson() {
            return person;
        }

        public Server getServer() {
            return server;
        }

        public Map<String, Object> getCustom() {
            return custom;
        }

        public String getFingerprint() {
            return fingerprint;
        }

        public String getTitle() {
            return title;
        }

        public Notifier getNotifier() {
            return notifier;
        }

        public UUID getUuid() {
            return uuid;
        }
    }
}
