package com.truevault.rollbar;

import com.truevault.rollbar.http.HttpItemClient;
import com.truevault.rollbar.http.RollbarResponse;
import com.truevault.rollbar.payload.Item;
import com.truevault.rollbar.payload.data.Data;
import com.truevault.rollbar.payload.data.Level;
import com.truevault.rollbar.payload.data.body.Body;
import com.truevault.rollbar.utilities.ArgumentNullException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The default implementation of RollbarReporter. Unless you have specific and unusual needs, this is probably what you
 * want to use. See {@link DefaultRollbarReporter.Builder} to make new instances.
 */
public class DefaultRollbarReporter implements RollbarReporter {
    @Nullable
    private final ItemFilter filter;
    @Nullable
    private final ItemTransformer transform;
    @Nonnull
    private final HttpItemClient sender;
    @Nonnull
    private final Supplier<Data.Builder> initialDataSupplier;
    @Nonnull
    private final String accessToken;
    @Nonnull
    private final Function<Throwable, Level> levelPolicy;
    @Nonnull
    private final String environment;

    private DefaultRollbarReporter(@Nullable ItemFilter filter, @Nullable ItemTransformer transform,
            @Nonnull HttpItemClient sender,
            @Nonnull Supplier<Data.Builder> initialDataSupplier, @Nonnull String accessToken,
            @Nonnull Function<Throwable, Level> levelPolicy, @Nonnull String environment) {
        this.filter = filter;
        this.transform = transform;
        this.sender = sender;
        this.initialDataSupplier = initialDataSupplier;
        this.accessToken = accessToken;
        this.levelPolicy = levelPolicy;
        this.environment = environment;
    }

    @Override
    public CompletableFuture<RollbarResponse> critical(Throwable error) {
        return log(error, null, null, Level.CRITICAL);
    }

    @Override
    public CompletableFuture<RollbarResponse> error(Throwable error) {
        return log(error, null, null, Level.ERROR);
    }

    @Override
    public CompletableFuture<RollbarResponse> warning(Throwable error) {
        return log(error, null, null, Level.WARNING);
    }

    @Override
    public CompletableFuture<RollbarResponse> info(Throwable error) {
        return log(error, null, null, Level.INFO);
    }

    @Override
    public CompletableFuture<RollbarResponse> debug(Throwable error) {
        return log(error, null, null, Level.DEBUG);
    }

    @Override
    public CompletableFuture<RollbarResponse> log(Throwable t) {
        return log(t, null, null, levelOf(t));
    }

    @Override
    public CompletableFuture<RollbarResponse> log(Throwable t, @Nonnull Level level) {
        return log(t, null, null, level);
    }

    @Override
    public CompletableFuture<RollbarResponse> critical(Throwable t, Map<String, Object> custom) {
        return log(t, custom, null, Level.CRITICAL);
    }

    @Override
    public CompletableFuture<RollbarResponse> error(Throwable t, Map<String, Object> custom) {
        return log(t, custom, null, Level.ERROR);
    }

    @Override
    public CompletableFuture<RollbarResponse> warning(Throwable t, Map<String, Object> custom) {
        return log(t, custom, null, Level.WARNING);
    }

    @Override
    public CompletableFuture<RollbarResponse> info(Throwable t, Map<String, Object> custom) {
        return log(t, custom, null, Level.INFO);
    }

    @Override
    public CompletableFuture<RollbarResponse> debug(Throwable t, Map<String, Object> custom) {
        return log(t, custom, null, Level.DEBUG);
    }

    @Override
    public CompletableFuture<RollbarResponse> log(Throwable t, Map<String, Object> custom) {
        return log(t, custom, null, levelOf(null));
    }

    @Override
    public CompletableFuture<RollbarResponse> log(Throwable t, Map<String, Object> custom, @Nonnull Level level) {
        return log(t, custom, null, level);
    }

    @Override
    public CompletableFuture<RollbarResponse> critical(Throwable t, String description) {
        return log(t, null, description, Level.CRITICAL);
    }

    @Override
    public CompletableFuture<RollbarResponse> error(Throwable t, String description) {
        return log(t, null, description, Level.ERROR);
    }

    @Override
    public CompletableFuture<RollbarResponse> warning(Throwable t, String description) {
        return log(t, null, description, Level.WARNING);
    }

    @Override
    public CompletableFuture<RollbarResponse> info(Throwable t, String description) {
        return log(t, null, description, Level.INFO);
    }

    @Override
    public CompletableFuture<RollbarResponse> debug(Throwable t, String description) {
        return log(t, null, description, Level.DEBUG);
    }

    @Override
    public CompletableFuture<RollbarResponse> log(Throwable t, String description) {
        return log(t, null, description, levelOf(t));
    }

    @Override
    public CompletableFuture<RollbarResponse> log(Throwable t, String description, @Nonnull Level level) {
        return log(t, null, description, level);
    }

    @Override
    public CompletableFuture<RollbarResponse> critical(Throwable t, Map<String, Object> custom,
            String description) {
        return log(t, custom, description, Level.CRITICAL);
    }

    @Override
    public CompletableFuture<RollbarResponse> error(Throwable t, Map<String, Object> custom, String description) {
        return log(t, custom, description, Level.ERROR);
    }

    @Override
    public CompletableFuture<RollbarResponse> warning(Throwable t, Map<String, Object> custom, String description) {
        return log(t, custom, description, Level.WARNING);
    }

    @Override
    public CompletableFuture<RollbarResponse> info(Throwable t, Map<String, Object> custom, String description) {
        return log(t, custom, description, Level.INFO);
    }

    @Override
    public CompletableFuture<RollbarResponse> debug(Throwable t, Map<String, Object> custom, String description) {
        return log(t, custom, description, Level.DEBUG);
    }

    @Override
    public CompletableFuture<RollbarResponse> log(Throwable t, Map<String, Object> custom, String description) {
        return log(t, custom, description, levelOf(null));
    }

    @Override
    public CompletableFuture<RollbarResponse> critical(String message) {
        return log(null, null, message, Level.CRITICAL);
    }

    @Override
    public CompletableFuture<RollbarResponse> error(String message) {
        return log(null, null, message, Level.ERROR);
    }

    @Override
    public CompletableFuture<RollbarResponse> warning(String message) {
        return log(null, null, message, Level.WARNING);
    }

    @Override
    public CompletableFuture<RollbarResponse> info(String message) {
        return log(null, null, message, Level.INFO);
    }

    @Override
    public CompletableFuture<RollbarResponse> debug(String message) {
        return log(null, null, message, Level.DEBUG);
    }

    @Override
    public CompletableFuture<RollbarResponse> log(String message) {
        return log(null, null, message, levelOf(null));
    }

    @Override
    public CompletableFuture<RollbarResponse> log(String message, @Nonnull Level level) {
        return log(null, null, message, level);
    }

    @Override
    public CompletableFuture<RollbarResponse> critical(String message, Map<String, Object> custom) {
        return log(null, custom, message, Level.CRITICAL);
    }

    @Override
    public CompletableFuture<RollbarResponse> error(String message, Map<String, Object> custom) {
        return log(null, custom, message, Level.ERROR);
    }

    @Override
    public CompletableFuture<RollbarResponse> warning(String message, Map<String, Object> custom) {
        return log(null, custom, message, Level.WARNING);
    }

    @Override
    public CompletableFuture<RollbarResponse> info(String message, Map<String, Object> custom) {
        return log(null, custom, message, Level.INFO);
    }

    @Override
    public CompletableFuture<RollbarResponse> debug(String message, Map<String, Object> custom) {
        return log(null, custom, message, Level.DEBUG);
    }

    @Override
    public CompletableFuture<RollbarResponse> log(String message, Map<String, Object> custom) {
        return log(null, custom, message, levelOf(null));
    }

    @Override
    public CompletableFuture<RollbarResponse> log(String message, Map<String, Object> custom, @Nonnull Level level) {
        return log(null, custom, message, level);
    }

    @Override
    public CompletableFuture<RollbarResponse> log(Data data, @Nullable Throwable t, @Nullable String description) {
        return sendItem(t, description, new Item(accessToken, data));
    }

    /**
     * Record a throwable or message with extra data at the level specified. At least ene of `error` or `description`
     * must be non-null. If error is null, `description` will be sent as a message. If error is non-null, description
     * will be sent as the description of the throwable. Custom data will be attached to message if the throwable is
     * null.
     *
     * @param t           the throwable (if any)
     * @param custom      the custom data (if any)
     * @param description the description of the throwable, or the message to send
     * @param level       the level to send it at
     */
    private CompletableFuture<RollbarResponse> log(@Nullable Throwable t, @Nullable Map<String, Object> custom,
            @Nullable String description, @Nonnull Level level) {
        return sendItem(t, description, buildItem(t, custom, description, level));
    }

    private CompletableFuture<RollbarResponse> sendItem(@Nullable Throwable t, @Nullable String description, Item item) {
        if (transform != null) {
            item = transform.transform(item, t, description);
        }

        if (filter == null || filter.shouldSend(item, t, description)) {
            return sender.send(item);
        }

        return CompletableFuture.completedFuture(RollbarResponse.filtered());
    }

    private Item buildItem(Throwable t, Map<String, Object> custom, String description, @Nonnull Level level) {
        Body body;
        if (t != null) {
            body = Body.fromThrowable(t, description);
        } else if (description != null) {
            body = Body.fromString(description, custom);
            custom = null;
        } else {
            throw new ArgumentNullException("error | description");
        }

        Data data = initialDataSupplier.get()
                .body(body)
                .level(level)
                .timestamp(Instant.now())
                .custom(custom)
                .environment(environment)
                .build();

        return new Item(accessToken, data);
    }

    /**
     * @param t the throwable
     * @return the level calculated by the level policy
     */
    @Nonnull
    private Level levelOf(@Nullable Throwable t) {
        return levelPolicy.apply(t);
    }

    public static class Builder {
        @Nonnull
        private final String environment;
        private ItemFilter filter;
        private ItemTransformer transformer;
        @Nonnull
        private final HttpItemClient httpItemClient;
        @Nonnull
        private Supplier<Data.Builder> initialDataSupplier;
        @Nonnull
        private final String accessToken;
        private Function<Throwable, Level> levelPolicy = (Throwable t) -> {
            if (t == null) {
                return Level.WARNING;
            }
            if (t instanceof Error) {
                return Level.CRITICAL;
            }
            return Level.ERROR;
        };

        /**
         * @param httpItemClient The {@link HttpItemClient} to use.
         * @param environment    The environment this is running in, e.g. "production" or "staging".
         * @param accessToken    An access token with scope "post_server_item" or "post_client_item". Probably "server"
         *                       unless your {@link Data#platform()} is "android" or "client". Must not be null or
         *                       whitespace.
         */
        public Builder(@Nonnull HttpItemClient httpItemClient, @Nonnull String environment,
                @Nonnull String accessToken) {
            this.httpItemClient = httpItemClient;
            this.environment = environment;
            String platform = System.getProperty("os.name") + " JVM " + System.getProperty("java.vendor") + " " +
                    System.getProperty("java.version");
            initialDataSupplier = () -> new Data.Builder().platform(platform).language("Java");
            this.accessToken = accessToken;
        }

        public Builder filter(ItemFilter filter) {
            this.filter = filter;
            return this;
        }

        public Builder transformer(ItemTransformer transformer) {
            this.transformer = transformer;
            return this;
        }

        /**
         * Configure how a new {@link Data.Builder} is built. This Supplier will be invoked for each new {@link Item}
         * that is reported to Rollbar, so it is a good place to configure any data that you want to always be present.
         *
         * Note that some things in Data.Builder (e.g. level, body, timestamp, etc) are overwritten after this is
         * invoked, so it isn't useful to set those.
         *
         * @param initialDataSupplier a new Supplier
         * @return this
         */
        public Builder initialDataSupplier(
                @Nonnull Supplier<Data.Builder> initialDataSupplier) {
            this.initialDataSupplier = initialDataSupplier;
            return this;
        }

        /**
         * Configure a {@link Function} to map Throwables (or null, if there is no Throwable) to a {@link Level}. The
         * provided Function must always return a non-null Level, even for a null Throwable.
         *
         * @param levelPolicy a new level policy
         * @return this
         */
        public Builder levelPolicy(Function<Throwable, Level> levelPolicy) {
            this.levelPolicy = levelPolicy;
            return this;
        }

        public DefaultRollbarReporter build() {
            return new DefaultRollbarReporter(filter, transformer, httpItemClient, initialDataSupplier, accessToken,
                    levelPolicy,
                    environment);
        }

        @Nonnull
        public String getEnvironment() {
            return environment;
        }

        public ItemFilter getFilter() {
            return filter;
        }

        public ItemTransformer getTransformer() {
            return transformer;
        }

        @Nonnull
        public HttpItemClient getHttpItemClient() {
            return httpItemClient;
        }

        @Nonnull
        public Supplier<Data.Builder> getInitialDataSupplier() {
            return initialDataSupplier;
        }

        @Nonnull
        public String getAccessToken() {
            return accessToken;
        }

        public Function<Throwable, Level> getLevelPolicy() {
            return levelPolicy;
        }
    }
}
