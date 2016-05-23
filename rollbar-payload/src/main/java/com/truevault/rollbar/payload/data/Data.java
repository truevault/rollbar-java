package com.truevault.rollbar.payload.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.truevault.rollbar.payload.data.body.Body;
import com.truevault.rollbar.utilities.ArgumentNullException;
import com.truevault.rollbar.utilities.InvalidLengthException;
import com.truevault.rollbar.utilities.Validate;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nonnull;

/**
 * Represents the actual data being posted to Rollbar
 */
public class Data {
    private final String environment;
    private final Body body;
    private final Level level;
    private final Long timestamp;
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
    private final String uuid;
    private final Notifier notifier;

    /**
     * Constructor
     * @param environment not nullable, string representing the current environment (e.g.: production, debug, test)
     * @param body not nullable, the actual data being sent to rollbar (not metadata, about the request, server, etc.)
     * @throws ArgumentNullException if neither body or environment is null, or if environment is empty or whitespace
     * @throws InvalidLengthException if the environment is longer than 255 characters
     */
    public Data(String environment, Body body) throws ArgumentNullException, InvalidLengthException {
        this(environment, body, null, (Long) null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    /**
     * Constructor
     * @param environment not nullable, string representing the current environment (e.g.: production, debug, test)
     * @param body not nullable, the actual data being sent to rollbar (not metadata, about the request, server, etc.)
     * @param level the rollbar error level
     * @param timestamp the moment the bug happened, visible in ui as client_timestamp
     * @param codeVersion the currently running version of the code
     * @param platform the platform running (most likely JVM and a version)
     * @param language the language running (most likely java, but any JVM language might be here)
     * @param framework the framework being run (e.g. Play, Spring, etc)
     * @param context custom identifier to help find where the error came from, Controller class name, for instance.
     * @param request data about the Http Request that caused this, if applicable
     * @param person data about the user that experienced the error, if possible
     * @param server data about the machine on which the error occurred
     * @param custom custom data that will aid in debugging the error
     * @param fingerprint override the default and custom grouping with a string, if over 255 characters will be hashed
     * @param title the title, max length 255 characters, overrides the default and custom ones set by rollbar
     * @param uuid override the error UUID, unique to each project, used to deduplicate occurrences
     * @param notifier information about this notifier, esp. if creating a framework specific notifier
     * @throws ArgumentNullException if environment or body is null
     * @throws InvalidLengthException if environment or title is over 255 characters, or uuid is over 32 characters
     */
    public Data(String environment, Body body, Level level, Date timestamp, String codeVersion, String platform, String language, String framework, String context, Request request, Person person, Server server, Map<String, Object> custom, String fingerprint, String title, UUID uuid, Notifier notifier) throws ArgumentNullException, InvalidLengthException {
        this(environment, body, level, timestamp == null ? null : (timestamp.getTime() / 1000), codeVersion, platform, language, framework, context, request, person, server, custom, fingerprint, title, uuid == null ? null : uuid.toString().replace("-", ""), notifier);
    }

    private Data(String environment, Body body, Level level, Long timestamp, String codeVersion, String platform, String language, String framework, String context, Request request, Person person, Server server, Map<String, Object> custom, String fingerprint, String title, String uuid, Notifier notifier) throws ArgumentNullException, InvalidLengthException {
        Validate.isNotNullOrWhitespace(environment, "environment");
        Validate.maxLength(environment, 255, "environment");
        Validate.isNotNull(body, "body");
        if (title != null) {
            Validate.maxLength(title, 255, "title");
        }
        if (uuid != null) {
            Validate.maxLength(uuid, 32, "uuid");
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
        this.custom = custom == null ? null : new LinkedHashMap<String, Object>(custom);
        this.fingerprint = fingerprint;
        this.title = title;
        this.uuid = uuid;
        this.notifier = notifier;
    }

    /**
     * @return string representing the current environment (e.g.: production, debug, test)
     */
    @JsonProperty("environment")
    public String environment() {
        return this.environment;
    }

    /**
    * Set environment in a copy of this Data
    * @param environment string representing the current environment (e.g.: production, debug, test)
    * @return copy of this Data with environment
    * @throws ArgumentNullException if environment is null
    * @throws InvalidLengthException if environment is over 255 characters
    */
    public Data environment(String environment) throws ArgumentNullException, InvalidLengthException {
        return new Data(environment, body, level, timestamp, codeVersion, platform, language, framework, context, request, person, server, custom, fingerprint, title, uuid, notifier);
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
    * Set body in a copy of this Data
    * @param body the actual data being sent to rollbar (not metadata, about the request, server, etc.)
    * @return copy of this Data with body overridden
    * @throws ArgumentNullException if body is null
    */
    public Data body(Body body) throws ArgumentNullException {
        return new Data(environment, body, level, timestamp, codeVersion, platform, language, framework, context, request, person, server, custom, fingerprint, title, uuid, notifier);
    }

    /**
     * @return the rollbar error level
     */
    @JsonProperty("level")
    public Level level() {
        return this.level;
    }

    /**
    * Set level in a copy of this Data
    * @param level the rollbar error level
    * @return copy of this Data with level overridden
    */
    public Data level(Level level) {
        return new Data(environment, body, level, timestamp, codeVersion, platform, language, framework, context, request, person, server, custom, fingerprint, title, uuid, notifier);
    }

    /**
     * @return the moment the bug happened, visible in ui as client_timestamp
     */
    @JsonProperty("timestamp")
    public Date timestamp() {
        return this.timestamp == null ? null : new Date(this.timestamp * 1000);
    }

    /**
    * Set date in a copy of this Data
    * @param date the moment the bug happened, visible in ui as client_timestamp
    * @return copy of this Data with date overridden
    */
    public Data timestamp(Date date) {
        return new Data(environment, body, level, date == null ? null : (date.getTime() / 1000), codeVersion, platform, language, framework, context, request, person, server, custom, fingerprint, title, uuid, notifier);
    }

    /**
     * @return the currently running version of the code
     */
    @JsonProperty("code_version")
    public String codeVersion() {
        return this.codeVersion;
    }

    /**
    * Set codeVersion in a copy of this Data
    * @param codeVersion the currently running version of the code
    * @return copy of this Data with codeVersion overridden
    */
    public Data codeVersion(String codeVersion) {
        return new Data(environment, body, level, timestamp, codeVersion, platform, language, framework, context, request, person, server, custom, fingerprint, title, uuid, notifier);
    }

    /**
     * @return the platform running (most likely JVM and a version)
     */
    @JsonProperty("platform")
    public String platform() {
        return this.platform;
    }

    /**
    * Set platform in a copy of this Data
    * @param platform the platform running (most likely JVM and a version)
    * @return copy of this Data with platform overridden
    */
    public Data platform(String platform) {
        return new Data(environment, body, level, timestamp, codeVersion, platform, language, framework, context, request, person, server, custom, fingerprint, title, uuid, notifier);
    }

    /**
     * @return the language running (most likely java, but any JVM language might be here)
     */
    @JsonProperty("language")
    public String language() {
        return this.language;
    }

    /**
    * Set language in a copy of this Data
    * @param language the language running (most likely java, but any JVM language might be here)
    * @return copy of this Data with language overridden
    */
    public Data language(String language) {
        return new Data(environment, body, level, timestamp, codeVersion, platform, language, framework, context, request, person, server, custom, fingerprint, title, uuid, notifier);
    }

    /**
     * @return the framework being run (e.g. Play, Spring, etc)
     */
    @JsonProperty("framework")
    public String framework() {
        return this.framework;
    }

    /**
    * Set framework in a copy of this Data
    * @param framework the framework being run (e.g. Play, Spring, etc)
    * @return copy of this Data with framework overridden
    */
    public Data framework(String framework) {
        return new Data(environment, body, level, timestamp, codeVersion, platform, language, framework, context, request, person, server, custom, fingerprint, title, uuid, notifier);
    }

    /**
     * @return custom identifier to help find where the error came from, Controller class name, for instance.
     */
    @JsonProperty("context")
    public String context() {
        return this.context;
    }

    /**
    * Set context in a copy of this Data
    * @param context custom identifier to help find where the error came from, Controller class name, for instance.
    * @return copy of this Data with context overridden
    */
    public Data context(String context) {
        return new Data(environment, body, level, timestamp, codeVersion, platform, language, framework, context, request, person, server, custom, fingerprint, title, uuid, notifier);
    }

    /**
     * @return data about the Http Request that caused this, if applicable
     */
    @JsonProperty("request")
    public Request request() {
        return this.request;
    }

    /**
    * Set request in a copy of this Data
    * @param request data about the Http Request that caused this, if applicable
    * @return copy of this Data with request overridden
    */
    public Data request(Request request) {
        return new Data(environment, body, level, timestamp, codeVersion, platform, language, framework, context, request, person, server, custom, fingerprint, title, uuid, notifier);
    }

    /**
     * @return data about the user that experienced the error, if possible
     */
    @JsonProperty("person")
    public Person person() {
        return this.person;
    }

    /**
    * Set person in a copy of this Data
    * @param person data about the user that experienced the error, if possible
    * @return copy of this Data with person overridden
    */
    public Data person(Person person) {
        return new Data(environment, body, level, timestamp, codeVersion, platform, language, framework, context, request, person, server, custom, fingerprint, title, uuid, notifier);
    }

    /**
     * @return data about the machine on which the error occurred
     */
    @JsonProperty("server")
    public Server server() {
        return this.server;
    }

    /**
    * Set server in a copy of this Data
    * @param server data about the machine on which the error occurred
    * @return copy of this Data with server overridden
    */
    public Data server(Server server) {
        return new Data(environment, body, level, timestamp, codeVersion, platform, language, framework, context, request, person, server, custom, fingerprint, title, uuid, notifier);
    }

    /**
     * @return custom data that will aid in debugging the error
     */
    @JsonProperty("custom")
    public Map<String, Object> custom() {
        return custom == null ? null : new LinkedHashMap<String, Object>(this.custom);
    }

    /**
    * Set custom in a copy of this Data
    * @param custom custom data that will aid in debugging the error
    * @return copy of this Data with custom overridden
    */
    public Data custom(Map<String, Object> custom) {
        return new Data(environment, body, level, timestamp, codeVersion, platform, language, framework, context, request, person, server, custom, fingerprint, title, uuid, notifier);
    }

    /**
     * @return override the default and custom grouping with a string, if over 255 characters will be hashed
     */
    @JsonProperty("fingerprint")
    public String fingerprint() {
        return this.fingerprint;
    }

    /**
    * Set fingerprint in a copy of this Data
    * @param fingerprint override the default and custom grouping with a string, if over 255 characters will be hashed
    * @return copy of this Data with fingerprint overridden
    */
    public Data fingerprint(String fingerprint) {
        return new Data(environment, body, level, timestamp, codeVersion, platform, language, framework, context, request, person, server, custom, fingerprint, title, uuid, notifier);
    }

    /**
     * @return the title, max length 255 characters, overrides the default and custom ones set by rollbar
     */
    @JsonProperty("title")
    public String title() {
        return this.title;
    }

    /**
    * Set title in a copy of this Data
    * @param title the title, max length 255 characters, overrides the default and custom ones set by rollbar
    * @return copy of this Data with title overridden
    * @throws InvalidLengthException if title is over 255 characters
    */
    public Data title(String title) throws InvalidLengthException {
        return new Data(environment, body, level, timestamp, codeVersion, platform, language, framework, context, request, person, server, custom, fingerprint, title, uuid, notifier);
    }

    /**
     * @return override the error UUID, unique to each project, used to deduplicate occurrences
     */
    @JsonProperty("uuid")
    public UUID uuid() {
        if (this.uuid == null) return null;
        // Thanks to http://stackoverflow.com/a/18987428/456188
        return UUID.fromString(this.uuid.replaceAll(
                "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",
                "$1-$2-$3-$4-$5"));
    }

    /**
    * Set uuid in a copy of this Data
    * @param uuid override the error UUID, unique to each project, used to deduplicate occurrences
    * @return copy of this Data with uuid overridden
    * @throws InvalidLengthException if uuid is over 32 characters
    */
    public Data uuid(UUID uuid) throws InvalidLengthException {
        return new Data(environment, body, level, timestamp(), codeVersion, platform, language, framework, context, request, person, server, custom, fingerprint, title, uuid, notifier);
    }

    /**
     * @return information about this notifier, esp. if creating a framework specific notifier
     */
    @JsonProperty("notifier")
    public Notifier notifier() {
        return this.notifier;
    }

    /**
    * Set notifier in a copy of this Data
    * @param notifier information about this notifier, esp. if creating a framework specific notifier
    * @return copy of this Data with notifier overridden
    */
    public Data notifier(Notifier notifier) {
        return new Data(environment, body, level, timestamp, codeVersion, platform, language, framework, context, request, person, server, custom, fingerprint, title, uuid, notifier);
    }

}
