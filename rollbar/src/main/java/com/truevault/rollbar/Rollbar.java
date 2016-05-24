package com.truevault.rollbar;

import com.truevault.rollbar.http.HttpItemClient;
import com.truevault.rollbar.http.RollbarResponse;
import com.truevault.rollbar.payload.Item;
import com.truevault.rollbar.payload.data.Data;
import com.truevault.rollbar.payload.data.Level;
import com.truevault.rollbar.payload.data.Notifier;
import com.truevault.rollbar.payload.data.Person;
import com.truevault.rollbar.payload.data.Request;
import com.truevault.rollbar.payload.data.Server;
import com.truevault.rollbar.payload.data.body.Body;
import com.truevault.rollbar.utilities.ArgumentNullException;
import com.truevault.rollbar.utilities.Validate;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;

/**
 * The notifier itself. Anything that can be statically set should be set by passed in through the constructor.
 * Anything that needs to be dynamically determined should be configured by subclassing and overriding the `getXXX`
 * methods.
 * There are
 */
public class Rollbar {
    private String accessToken;
    private String environment;
    private String codeVersion;
    private String platform;
    private String language;
    private String framework;
    private String context;
    private Request request;
    private Person person;
    private Server server;
    private Map<String, Object> custom;
    private Notifier notifier;
    @Nullable
    private ItemFilter filter;
    private ItemTransform transform;
    private HttpItemClient sender;

    /**
     * Construct notifier, defaults for everything but Sender.
     * @param accessToken not nullable, the access token to send payloads to
     * @param environment not nullable, the environment to send payloads under
     * @param sender the sender to use.
     */
    public Rollbar(String accessToken, String environment, HttpItemClient sender) {
        this(accessToken, environment, sender, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    /**
     * Construct notifier with static values for all configuration options set. Anything left null will use the default
     * value. If appropriate.
     * @param accessToken not nullable, the access token to send payloads to
     * @param environment not nullable, the environment to send payloads under
     * @param sender the sender to use.
     * @param codeVersion the version of the code currently running. If code checked out on server: `git rev-parse HEAD`
     * @param platform the platform you're running. (JVM version, or similar).
     * @param language the main language you're running ("java" by default, override w/ "clojure", "scala" etc.).
     * @param framework the framework you're using ("Play", "Spring", etc.).
     * @param context a mnemonic for finding the code responsible (e.g. controller name, module name)
     * @param request the HTTP request that triggered this error. Can be set if the IOC container can work per-request.
     * @param person the affected person. Can be set if the IOC container can work per-request.
     * @param server info about this server. This can be statically set.
     * @param custom custom info to send with *every* error. Can be dynamically or statically set.
     * @param notifier information about this notifier. Default {@code new Notifier()} ({@link Notifier}.
     * @param filter filter used to determine if you will send payload. Receives *transformed* payload.
     * @param transform alter payload before sending.
     */
    public Rollbar(String accessToken, String environment, HttpItemClient sender, String codeVersion, String platform,
                   String language, String framework, String context, Request request, Person person, Server server,
                   Map<String, Object> custom, Notifier notifier, ItemFilter filter, ItemTransform transform) {
        Validate.isNotNullOrWhitespace(accessToken, "accessToken");
        Validate.isNotNullOrWhitespace(environment, "environment");


        this.sender = sender;
        this.accessToken = accessToken;
        this.environment = environment;
        this.codeVersion = codeVersion;
        this.platform = platform;
        this.language = language;
        this.framework = framework == null ? "java" : framework;
        this.context = context;
        this.request = request;
        this.person = person;
        this.server = server;
        this.notifier = notifier == null ? new Notifier() : notifier;
        this.filter = filter;
        this.transform = transform;

        if (custom != null) {
            this.custom = new LinkedHashMap<String, Object>(custom);
        }
    }

    /**
     * Record a critical error
     * @param error the error
     */
    public CompletableFuture<RollbarResponse> critical(Throwable error) {
        return log(error, null, null, Level.CRITICAL);
    }

    /**
     * Record an error
     * @param error the error
     */
    public CompletableFuture<RollbarResponse> error(Throwable error) {
        return log(error, null, null, Level.ERROR);
    }

    /**
     * Record an error as a warning
     * @param error the error
     */
    public CompletableFuture<RollbarResponse> warning(Throwable error) {
        return log(error, null, null, Level.WARNING);
    }

    /**
     * Record an error as an info
     * @param error the error
     */
    public CompletableFuture<RollbarResponse> info(Throwable error) {
        return log(error, null, null, Level.INFO);
    }

    /**
     * Record an error as debugging information
     * @param error the error
     */
    public CompletableFuture<RollbarResponse> debug(Throwable error) {
        return log(error, null, null, Level.DEBUG);
    }

    /**
     * Log an error at the level returned by {@link Rollbar#level}
     * @param error the error
     */
    public CompletableFuture<RollbarResponse> log(Throwable error) {
        return log(error, null, null, null);
    }

    /**
     * Log an error at level specified.
     * @param error the error
     * @param level the level of the error
     */
    public CompletableFuture<RollbarResponse> log(Throwable error, Level level) {
        return log(error, null, null, level);
    }

    /**
     * Record a critical error with extra information attached
     * @param error the error
     * @param custom the extra information
     */
    public CompletableFuture<RollbarResponse> critical(Throwable error, Map<String, Object> custom) {
        return log(error, custom, null, Level.CRITICAL);
    }

    /**
     * Record an error with extra information attached
     * @param error the error
     * @param custom the extra information
     */
    public CompletableFuture<RollbarResponse> error(Throwable error, Map<String, Object> custom) {
        return log(error, custom, null, Level.ERROR);
    }

    /**
     * Record a warning error with extra information attached
     * @param error the error
     * @param custom the extra information
     */
    public CompletableFuture<RollbarResponse> warning(Throwable error, Map<String, Object> custom) {
        return log(error, custom, null, Level.WARNING);
    }

    /**
     * Record an info error with extra information attached
     * @param error the error
     * @param custom the extra information
     */
    public CompletableFuture<RollbarResponse> info(Throwable error, Map<String, Object> custom) {
        return log(error, custom, null, Level.INFO);
    }

    /**
     * Record a debug error with extra information attached
     * @param error the error
     * @param custom the extra information
     */
    public CompletableFuture<RollbarResponse> debug(Throwable error, Map<String, Object> custom) {
        return log(error, custom, null, Level.DEBUG);
    }

    /**
     * Record an error with extra information attached at the default level retured by {@link Rollbar#level}
     * @param error the error
     * @param custom the extra information
     */
    public CompletableFuture<RollbarResponse> log(Throwable error, Map<String, Object> custom) {
        return log(error, custom, null, null);
    }

    /**
     * Record an error with extra information attached at the level specified
     * @param error the error
     * @param custom the extra information
     * @param level the level
     */
    public CompletableFuture<RollbarResponse> log(Throwable error, Map<String, Object> custom, Level level) {
        return log(error, custom, null, level);
    }

    /**
     * Record a critical error with human readable description
     * @param error the error
     * @param description human readable description of error
     */
    public CompletableFuture<RollbarResponse> critical(Throwable error, String description) {
        return log(error, null, description, Level.CRITICAL);
    }

    /**
     * Record an error with human readable description
     * @param error the error
     * @param description human readable description of error
     */
    public CompletableFuture<RollbarResponse> error(Throwable error, String description) {
        return log(error, null, description, Level.ERROR);
    }

    /**
     * Record a warning with human readable description
     * @param error the error
     * @param description human readable description of error
     */
    public CompletableFuture<RollbarResponse> warning(Throwable error, String description) {
        return log(error, null, description, Level.WARNING);
    }

    /**
     * Record an info error with human readable description
     * @param error the error
     * @param description human readable description of error
     */
    public CompletableFuture<RollbarResponse> info(Throwable error, String description) {
        return log(error, null, description, Level.INFO);
    }

    /**
     * Record a debug error with human readable description
     * @param error the error
     * @param description human readable description of error
     */
    public CompletableFuture<RollbarResponse> debug(Throwable error, String description) {
        return log(error, null, description, Level.DEBUG);
    }

    /**
     * Record an error with human readable description at the default level returned by {@link Rollbar#level}
     * @param error the error
     * @param description human readable description of error
     */
    public CompletableFuture<RollbarResponse> log(Throwable error, String description) {
        return log(error, null, description, null);
    }

    /**
     * Record a debug error with human readable description at the specified level
     * @param error the error
     * @param description human readable description of error
     * @param level the level
     */
    public CompletableFuture<RollbarResponse> log(Throwable error, String description, Level level) {
        return log(error, null, description, level);
    }

    /**
     * Record a critical error with custom parameters and human readable description
     * @param error the error
     * @param custom the custom data
     * @param description the human readable description of error
     */
    public CompletableFuture<RollbarResponse> critical(Throwable error, Map<String, Object> custom, String description) {
        return log(error, custom, description, Level.CRITICAL);
    }

    /**
     * Record an error with custom parameters and human readable description
     * @param error the error
     * @param custom the custom data
     * @param description the human readable description of error
     */
    public CompletableFuture<RollbarResponse> error(Throwable error, Map<String, Object> custom, String description) {
        return log(error, custom, description, Level.ERROR);
    }

    /**
     * Record a warning error with custom parameters and human readable description
     * @param error the error
     * @param custom the custom data
     * @param description the human readable description of error
     */
    public CompletableFuture<RollbarResponse> warning(Throwable error, Map<String, Object> custom, String description) {
        return log(error, custom, description, Level.WARNING);
    }

    /**
     * Record an info error with custom parameters and human readable description
     * @param error the error
     * @param custom the custom data
     * @param description the human readable description of error
     */
    public CompletableFuture<RollbarResponse> info(Throwable error, Map<String, Object> custom, String description) {
        return log(error, custom, description, Level.INFO);
    }

    /**
     * Record a debug error with custom parameters and human readable description
     * @param error the error
     * @param custom the custom data
     * @param description the human readable description of error
     */
    public CompletableFuture<RollbarResponse> debug(Throwable error, Map<String, Object> custom, String description) {
        return log(error, custom, description, Level.DEBUG);
    }

    /**
     * Record an error with custom parameters and human readable description at the default level returned by
     * {@link Rollbar#level}
     * @param error the error
     * @param custom the custom data
     * @param description the human readable description of error
     */
    public CompletableFuture<RollbarResponse> log(Throwable error, Map<String, Object> custom, String description) {
        return log(error, custom, description, null);
    }

    /**
     * Record a critical message
     * @param message the message
     */
    public CompletableFuture<RollbarResponse> critical(String message) {
        return log(null, null, message, Level.CRITICAL);
    }

    /**
     * Record an error message
     * @param message the message
     */
    public CompletableFuture<RollbarResponse> error(String message) {
        return log(null, null, message, Level.ERROR);
    }

    /**
     * Record a warning message
     * @param message the message
     */
    public CompletableFuture<RollbarResponse> warning(String message) {
        return log(null, null, message, Level.WARNING);
    }

    /**
     * Record an informational message
     * @param message the message
     */
    public CompletableFuture<RollbarResponse> info(String message) {
        return log(null, null, message, Level.INFO);
    }

    /**
     * Record a debugging message
     * @param message the message
     */
    public CompletableFuture<RollbarResponse> debug(String message) {
        return log(null, null, message, Level.DEBUG);
    }

    /**
     * Record a debugging message at the level returned by {@link Rollbar#level} (WARNING unless level is overriden)
     * @param message the message
     */
    public CompletableFuture<RollbarResponse> log(String message) {
        return log(null, null, message, null);
    }

    /**
     * Record a message at the level specified
     * @param message the message
     * @param level the level
     */
    public CompletableFuture<RollbarResponse> log(String message, Level level) {
        return log(null, null, message, level);
    }

    /**
     * Record a critical message with extra information attached
     * @param message the message
     * @param custom the extra information
     */
    public CompletableFuture<RollbarResponse> critical(String message, Map<String, Object> custom) {
        return log(null, custom, message, Level.CRITICAL);
    }

    /**
     * Record a error message with extra information attached
     * @param message the message
     * @param custom the extra information
     */
    public CompletableFuture<RollbarResponse> error(String message, Map<String, Object> custom) {
        return log(null, custom, message, Level.ERROR);
    }

    /**
     * Record a warning message with extra information attached
     * @param message the message
     * @param custom the extra information
     */
    public CompletableFuture<RollbarResponse> warning(String message, Map<String, Object> custom) {
        return log(null, custom, message, Level.WARNING);
    }

    /**
     * Record an informational message with extra information attached
     * @param message the message
     * @param custom the extra information
     */
    public CompletableFuture<RollbarResponse> info(String message, Map<String, Object> custom) {
        return log(null, custom, message, Level.INFO);
    }

    /**
     * Record a debugging message with extra information attached
     * @param message the message
     * @param custom the extra information
     */
    public CompletableFuture<RollbarResponse> debug(String message, Map<String, Object> custom) {
        return log(null, custom, message, Level.DEBUG);
    }

    /**
     * Record a message with extra information attached at the default level returned by {@link Rollbar#level}, (WARNING
     * unless level overriden).
     * @param message the message
     * @param custom the extra information
     */
    public CompletableFuture<RollbarResponse> log(String message, Map<String, Object> custom) {
        return log(null, custom, message, null);
    }

    /**
     * Record a message with extra infomation attached at the specified level
     * @param message the message
     * @param custom the extra information
     * @param level the level
     */
    public CompletableFuture<RollbarResponse> log(String message, Map<String, Object> custom, Level level) {
        return log(null, custom, message, level);
    }

    /**
     * Record an error or message with extra data at the level specified. At least ene of `error` or `description` must
     * be non-null. If error is null, `description` will be sent as a message. If error is non-null, description will be
     * sent as the description of the error.
     * Custom data will be attached to message if the error is null. Custom data will extend whatever
     * {@link Rollbar#custom} returns.
     * @param error the error (if any)
     * @param custom the custom data (if any)
     * @param description the description of the error, or the message to send
     * @param level the level to send it at
     */
     private CompletableFuture<RollbarResponse> log(Throwable error, Map<String, Object> custom, String description, Level level) {
        Item p = buildPayload(error, custom, description, level);
       return sendPayload(p, error, description);
    }

    private Item buildPayload(Throwable error, Map<String, Object> custom, String description, Level level) {
        Body body;
        if (error != null) {
            body = Body.fromError(error, description);
        } else if (description != null) {
            body = Body.fromString(description, custom);
            custom = null;
        } else {
            throw new ArgumentNullException("error | description");
        }

        level = level == null ? level(error) : level;

        Map<String, Object> defaultCustom = getCustom();
        if (defaultCustom != null || custom != null) {
            Map<String, Object> finalCustom = new LinkedHashMap<String, Object>();
            if (defaultCustom != null) {
                finalCustom.putAll(defaultCustom);
            }
            if (custom != null) {
                finalCustom.putAll(custom);
            }
            custom = finalCustom;
        }

        Data data = new Data(getEnvironment(), body, level, new Date(), getCodeVersion(), getPlatform(),
                             getLanguage(), getFramework(), getContext(), getRequest(), getPerson(), getServer(),
                             custom, null, null, null, getNotifier());
        Item p = new Item(getAccessToken(), data);

        if (getTransform() != null) {
            return getTransform().transform(p, error, description);
        }

        return p;
    }

    private CompletableFuture<RollbarResponse> sendPayload(Item p, Throwable error, String description) {
        if (filter == null || filter.shouldSend(p, error, description)) {
            return sender.send(p);
        }

        return CompletableFuture.completedFuture(RollbarResponse.filtered());
    }

    /**
     * Get the level of the error or message. By default: CRITICAL for {@link Error}, ERROR for other {@link Throwable},
     * WARNING for messages. Override to change this default.
     * @param error the error
     * @return the level
     */
    public Level level(Throwable error) {
        if (error == null) {
            return Level.WARNING;
        }
        if (error instanceof Error) {
            return Level.CRITICAL;
        }
        return Level.ERROR;
    }

    /**
     * @return the access token
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Set the accessToken
     * Accessible for subclasses and IOC containers like spring.
     * In Subclasses DO NOT USE OUTSIDE OF CONSTRUCTOR
     * @param accessToken the new access token
     */
    protected void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Get a copy of this Rollbar with accessToken overridden
     * @param accessToken the access token
     * @return a copy of this Rollbar with access token overridden
     * @throws ArgumentNullException if environment is null
     */
    public Rollbar accessToken(String accessToken) throws ArgumentNullException {
        return new Rollbar(accessToken, environment, sender, codeVersion, platform, language, framework, context, request, person, server, custom, notifier, filter, transform);
    }

    /**
     * @return the environment
     */
    public String getEnvironment() {
        return environment;
    }

    /**
     * Set the environment
     * Accessible for subclasses and IOC containers like spring.
     * In Subclasses DO NOT USE OUTSIDE OF CONSTRUCTOR
     * @param environment the new environment
     */
    protected void setEnvironment(String environment) {
        this.environment = environment;
    }

    /**
     * Get a copy of this Rollbar with environment overridden
     * @param environment the new environment
     * @return a copy of this Rollbar with environment overridden
     * @throws ArgumentNullException if environment is null
     */
    public Rollbar environment(String environment) throws ArgumentNullException {
        return new Rollbar(accessToken, environment, sender, codeVersion, platform, language, framework, context, request, person, server, custom, notifier, filter, transform);
    }

    /**
     * Get a copy of this Rollbar with sender overridden
     * @param sender the new sender
     * @return a copy of this Rollbar with sender overridden
     */
    public Rollbar sender(HttpItemClient sender) {
        return new Rollbar(accessToken, environment, sender, codeVersion, platform, language, framework, context, request, person, server, custom, notifier, filter, transform);
    }

    /**
     * @return the code version
     */
    public String getCodeVersion() {
        return codeVersion;
    }

    /**
     * Set the code version
     * Accessible for subclasses and IOC containers like spring.
     * In Subclasses DO NOT USE OUTSIDE OF CONSTRUCTOR
     * @param codeVersion the new version of the code running of the server
     */
    protected void setCodeVersion(String codeVersion) {
        this.codeVersion = codeVersion;
    }

    /**
     * Get a copy of this Rollbar with code version overridden
     * @param codeVersion the new code version
     * @return a copy of this Rollbar with code version overridden
     */
    public Rollbar codeVersion(String codeVersion) {
        return new Rollbar(accessToken, environment, sender, codeVersion, platform, language, framework, context, request, person, server, custom, notifier, filter, transform);
    }

    /**
     * @return the platform
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * Set the platform
     * Accessible for subclasses and IOC containers like spring.
     * In Subclasses DO NOT USE OUTSIDE OF CONSTRUCTOR
     * @param platform the new platform
     */
    protected void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * Get a copy of this Rollbar with platform overridden
     * @param platform the new platform
     * @return a copy of this Rollbar with platform overridden
     */
    public Rollbar platform(String platform) {
        return new Rollbar(accessToken, environment, sender, codeVersion, platform, language, framework, context, request, person, server, custom, notifier, filter, transform);
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Set the language
     * Accessible for subclasses and IOC containers like spring.
     * In Subclasses DO NOT USE OUTSIDE OF CONSTRUCTOR
     * @param language the new language
     */
    protected void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Get a copy of this Rollbar with language overridden
     * @param language the new language
     * @return a copy of this Rollbar with language overridden
     */
    public Rollbar language(String language) {
        return new Rollbar(accessToken, environment, sender, codeVersion, platform, language, framework, context, request, person, server, custom, notifier, filter, transform);
    }

    /**
     * @return the framework
     */
    public String getFramework() {
        return framework;
    }

    /**
     * Set the framework
     * Accessible for subclasses and IOC containers like spring.
     * In Subclasses DO NOT USE OUTSIDE OF CONSTRUCTOR
     * @param framework the new framework
     */
    protected void setFramework(String framework) {
        this.framework = framework;
    }

    /**
     * Get a copy of this Rollbar with framework overridden
     * @param framework the new platform
     * @return a copy of this Rollbar with framework overridden
     */
    public Rollbar framework(String framework) {
        return new Rollbar(accessToken, environment, sender, codeVersion, platform, language, framework, context, request, person, server, custom, notifier, filter, transform);
    }

    /**
     * @return the context
     */
    public String getContext() {
        return context;
    }

    /**
     * Set the context
     * Accessible for subclasses and IOC containers like spring.
     * In Subclasses DO NOT USE OUTSIDE OF CONSTRUCTOR
     * @param context the new context
     */
    protected void setContext(String context) {
        this.context = context;
    }

    /**
     * Get a copy of this Rollbar with context overridden
     * @param context the new platform
     * @return a copy of this Rollbar with context overridden
     */
    public Rollbar context(String context) {
        return new Rollbar(accessToken, environment, sender, codeVersion, platform, language, framework, context, request, person, server, custom, notifier, filter, transform);
    }

    /**
     * @return the request
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Set the request
     * Accessible for subclasses and IOC containers like spring.
     * In Subclasses DO NOT USE OUTSIDE OF CONSTRUCTOR
     * @param request the new request
     */
    protected void setRequest(Request request) {
        this.request = request;
    }

    /**
     * Get a copy of this Rollbar with request overridden
     * @param request the new platform
     * @return a copy of this Rollbar with request overridden
     */
    public Rollbar request(Request request) {
        return new Rollbar(accessToken, environment, sender, codeVersion, platform, language, framework, context, request, person, server, custom, notifier, filter, transform);
    }

    /**
     * @return the person
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Set the person
     * Accessible for subclasses and IOC containers like spring.
     * In Subclasses DO NOT USE OUTSIDE OF CONSTRUCTOR
     * @param person the new person
     */
    protected void setPerson(Person person) {
        this.person = person;
    }

    /**
     * Get a copy of this Rollbar with person overridden
     * @param person the new platform
     * @return a copy of this Rollbar with person overridden
     */
    public Rollbar person(Person person) {
        return new Rollbar(accessToken, environment, sender, codeVersion, platform, language, framework, context, request, person, server, custom, notifier, filter, transform);
    }

    /**
     * @return the server
     */
    public Server getServer() {
        return server;
    }

    /**
     * Set the server
     * Accessible for subclasses and IOC containers like spring.
     * In Subclasses DO NOT USE OUTSIDE OF CONSTRUCTOR
     * @param server the new server
     */
    protected void setServer(Server server) {
        this.server = server;
    }

    /**
     * Get a copy of this Rollbar with server overridden
     * @param server the new platform
     * @return a copy of this Rollbar with server overridden
     */
    public Rollbar server(Server server) {
        return new Rollbar(accessToken, environment, sender, codeVersion, platform, language, framework, context, request, person, server, custom, notifier, filter, transform);
    }

    /**
     * @return the custom data
     */
    public Map<String, Object> getCustom() {
        return custom;
    }

    /**
     * Set the custom data
     * Accessible for subclasses and IOC containers like spring.
     * In Subclasses DO NOT USE OUTSIDE OF CONSTRUCTOR
     * @param custom the new custom data
     */
    protected void setCustom(Map<String, Object> custom) {
        this.custom = custom;
    }

    /**
     * Get a copy of this Rollbar with custom overridden
     * @param custom the new platform
     * @return a copy of this Rollbar with custom overridden
     */
    public Rollbar custom(Map<String, Object> custom) {
        return new Rollbar(accessToken, environment, sender, codeVersion, platform, language, framework, context, request, person, server, custom, notifier, filter, transform);
    }

    /**
     * @return the notifier
     */
    public Notifier getNotifier() {
        return notifier;
    }

    /**
     * Set the notifier
     * Accessible for subclasses and IOC containers like spring.
     * In Subclasses DO NOT USE OUTSIDE OF CONSTRUCTOR
     * @param notifier the new notifier
     */
    protected void setNotifier(Notifier notifier) {
        this.notifier = notifier;
    }

    /**
     * Get a copy of this Rollbar with notifier overridden
     * @param notifier the new platform
     * @return a copy of this Rollbar with notifier overridden
     */
    public Rollbar notifier(Notifier notifier) {
        return new Rollbar(accessToken, environment, sender, codeVersion, platform, language, framework, context, request, person, server, custom, notifier, filter, transform);
    }

    /**
     * @return the transform
     */
    public ItemTransform getTransform() {
        return transform;
    }

    /**
     * Set the transform
     * Accessible for subclasses and IOC containers like spring.
     * In Subclasses DO NOT USE OUTSIDE OF CONSTRUCTOR
     * @param transform the new transform
     */
    protected void setTransform(ItemTransform transform) {
        this.transform = transform;
    }

    /**
     * Get a copy of this Rollbar with transform overridden
     * @param transform the new platform
     * @return a copy of this Rollbar with transform overridden
     */
    public Rollbar transform(ItemTransform transform) {
        return new Rollbar(accessToken, environment, sender, codeVersion, platform, language, framework, context, request, person, server, custom, notifier, filter, transform);
    }

}
