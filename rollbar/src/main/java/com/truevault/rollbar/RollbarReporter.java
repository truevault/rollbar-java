package com.truevault.rollbar;

import com.truevault.rollbar.http.RollbarResponse;
import com.truevault.rollbar.payload.data.Data;
import com.truevault.rollbar.payload.data.Level;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * All of these methods return very quickly and execute the corresponding request in the background. If you want to wait
 * for the request to complete, call {@link Future#get()}. If you want to log errors or perform some other action
 * asynchronously but not wait for completion, you could use one of the many options CompletableFuture gives you, like
 * {@link CompletableFuture#whenComplete(BiConsumer)}.
 */
public interface RollbarReporter {
    /**
     * Record a throwable as a critical error
     *
     * @param error the throwable
     */
    CompletableFuture<RollbarResponse> critical(Throwable error);

    /**
     * Record a throwable as an error
     *
     * @param error the throwable
     */
    CompletableFuture<RollbarResponse> error(Throwable error);

    /**
     * Record a throwable as a warning
     *
     * @param error the throwable
     */
    CompletableFuture<RollbarResponse> warning(Throwable error);

    /**
     * Record a throwable as an info
     *
     * @param error the throwable
     */
    CompletableFuture<RollbarResponse> info(Throwable error);

    /**
     * Record a throwable as debugging information
     *
     * @param error the throwable
     */
    CompletableFuture<RollbarResponse> debug(Throwable error);

    /**
     * Log a throwable at the level calculated by the level policy
     *
     * @param t the throwable
     */
    CompletableFuture<RollbarResponse> log(Throwable t);

    /**
     * Log a throwable at level specified.
     *
     * @param t     the throwable
     * @param level the level of the throwable
     */
    CompletableFuture<RollbarResponse> log(Throwable t, @Nonnull Level level);

    /**
     * Record a critical error with extra information attached
     *
     * @param t      the throwable
     * @param custom the extra information
     */
    CompletableFuture<RollbarResponse> critical(Throwable t, Map<String, Object> custom);

    /**
     * Record a throwable with extra information attached
     *
     * @param t      the throwable
     * @param custom the extra information
     */
    CompletableFuture<RollbarResponse> error(Throwable t, Map<String, Object> custom);

    /**
     * Record a warning error with extra information attached
     *
     * @param t      the throwable
     * @param custom the extra information
     */
    CompletableFuture<RollbarResponse> warning(Throwable t, Map<String, Object> custom);

    /**
     * Record an info throwablewith extra information attached
     *
     * @param t      the throwable
     * @param custom the extra information
     */
    CompletableFuture<RollbarResponse> info(Throwable t, Map<String, Object> custom);

    /**
     * Record a debug throwable with extra information attached
     *
     * @param t      the throwable
     * @param custom the extra information
     */
    CompletableFuture<RollbarResponse> debug(Throwable t, Map<String, Object> custom);

    /**
     * Record a throwable with extra information attached at the implementation-specific default level
     *
     * @param t      the throwable
     * @param custom the extra information
     */
    CompletableFuture<RollbarResponse> log(Throwable t, Map<String, Object> custom);

    /**
     * Record a throwable with extra information attached at the level specified
     *
     * @param t      the throwable
     * @param custom the extra information
     * @param level  the level
     */
    CompletableFuture<RollbarResponse> log(Throwable t, Map<String, Object> custom, @Nonnull Level level);

    /**
     * Record a critical throwable with human readable description
     *
     * @param t           the throwable
     * @param description human readable description of the error
     */
    CompletableFuture<RollbarResponse> critical(Throwable t, String description);

    /**
     * Record a throwable with human readable description
     *
     * @param t           the throwable
     * @param description human readable description of the error
     */
    CompletableFuture<RollbarResponse> error(Throwable t, String description);

    /**
     * Record a warning with human readable description
     *
     * @param t           the throwable
     * @param description human readable description of the error
     */
    CompletableFuture<RollbarResponse> warning(Throwable t, String description);

    /**
     * Record an info error with human readable description
     *
     * @param t           the throwable
     * @param description human readable description of the error
     */
    CompletableFuture<RollbarResponse> info(Throwable t, String description);

    /**
     * Record a debug error with human readable description
     *
     * @param t           the throwable
     * @param description human readable description of the error
     */
    CompletableFuture<RollbarResponse> debug(Throwable t, String description);

    /**
     * Record a throwable with human readable description at the implementation-specific default level
     *
     * @param t           the throwable
     * @param description human readable description of the error
     */
    CompletableFuture<RollbarResponse> log(Throwable t, String description);

    /**
     * Record a debug error with human readable description at the specified level
     *
     * @param t           the throwable
     * @param description human readable description of the error
     * @param level       the level
     */
    CompletableFuture<RollbarResponse> log(Throwable t, String description, @Nonnull Level level);

    /**
     * Record a critical error with custom parameters and human readable description
     *
     * @param t           the throwable
     * @param custom      the custom data
     * @param description the human readable description of the error
     */
    CompletableFuture<RollbarResponse> critical(Throwable t, Map<String, Object> custom, String description);

    /**
     * Record a throwable with custom parameters and human readable description
     *
     * @param t           the throwable
     * @param custom      the custom data
     * @param description the human readable description of the error
     */
    CompletableFuture<RollbarResponse> error(Throwable t, Map<String, Object> custom, String description);

    /**
     * Record a warning error with custom parameters and human readable description
     *
     * @param t           the throwable
     * @param custom      the custom data
     * @param description the human readable description of the error
     */
    CompletableFuture<RollbarResponse> warning(Throwable t, Map<String, Object> custom, String description);

    /**
     * Record an info error with custom parameters and human readable description
     *
     * @param t           the throwable
     * @param custom      the custom data
     * @param description the human readable description of the error
     */
    CompletableFuture<RollbarResponse> info(Throwable t, Map<String, Object> custom, String description);

    /**
     * Record a debug error with custom parameters and human readable description
     *
     * @param t           the throwable
     * @param custom      the custom data
     * @param description the human readable description of the error
     */
    CompletableFuture<RollbarResponse> debug(Throwable t, Map<String, Object> custom, String description);

    /**
     * Record a throwable with custom parameters and human readable description at the implementation-specific default
     * level
     *
     * @param t           the throwable
     * @param custom      the custom data
     * @param description the human readable description of the error
     */
    CompletableFuture<RollbarResponse> log(Throwable t, Map<String, Object> custom, String description);

    /**
     * Record a critical message
     *
     * @param message the message
     */
    CompletableFuture<RollbarResponse> critical(String message);

    /**
     * Record a throwable message
     *
     * @param message the message
     */
    CompletableFuture<RollbarResponse> error(String message);

    /**
     * Record a warning message
     *
     * @param message the message
     */
    CompletableFuture<RollbarResponse> warning(String message);

    /**
     * Record an informational message
     *
     * @param message the message
     */
    CompletableFuture<RollbarResponse> info(String message);

    /**
     * Record a debugging message
     *
     * @param message the message
     */
    CompletableFuture<RollbarResponse> debug(String message);

    /**
     * Record a debugging message at the default level
     *
     * @param message the message
     */
    CompletableFuture<RollbarResponse> log(String message);

    /**
     * Record a message at the level specified
     *
     * @param message the message
     * @param level   the level
     */
    CompletableFuture<RollbarResponse> log(String message, @Nonnull Level level);

    /**
     * Record a critical message with extra information attached
     *
     * @param message the message
     * @param custom  the extra information
     */
    CompletableFuture<RollbarResponse> critical(String message, Map<String, Object> custom);

    /**
     * Record a error message with extra information attached
     *
     * @param message the message
     * @param custom  the extra information
     */
    CompletableFuture<RollbarResponse> error(String message, Map<String, Object> custom);

    /**
     * Record a warning message with extra information attached
     *
     * @param message the message
     * @param custom  the extra information
     */
    CompletableFuture<RollbarResponse> warning(String message, Map<String, Object> custom);

    /**
     * Record an informational message with extra information attached
     *
     * @param message the message
     * @param custom  the extra information
     */
    CompletableFuture<RollbarResponse> info(String message, Map<String, Object> custom);

    /**
     * Record a debugging message with extra information attached
     *
     * @param message the message
     * @param custom  the extra information
     */
    CompletableFuture<RollbarResponse> debug(String message, Map<String, Object> custom);

    /**
     * Record a message with extra information attached at the default level returned by the level policy
     *
     * @param message the message
     * @param custom  the extra information
     */
    CompletableFuture<RollbarResponse> log(String message, Map<String, Object> custom);

    /**
     * Record a message with extra information attached at the specified level
     *
     * @param message the message
     * @param custom  the extra information
     * @param level   the level
     */
    CompletableFuture<RollbarResponse> log(String message, Map<String, Object> custom, @Nonnull Level level);

    /**
     * Record a fully formed Data object. If the other methods in this interface don't express what you need, and the
     * other configuration options won't let you do what you need to do, this will give you control over all the details
     * of what is sent.
     *
     * @param data        a Data object with at least environment and body set
     * @param t           the throwable responsible, if any
     * @param description the description, if any
     */
    CompletableFuture<RollbarResponse> log(Data data, @Nullable Throwable t, @Nullable String description);

    /**
     * A slightly less flexible but more convenient version of {@link RollbarReporter#log(Data, Throwable, String)}.
     * Most cases that call for manually configuring the Data object still don't care about setting the timestamp or
     * environment, so this method adds those two parts (environment and timestamp) to the provided Builder.
     *
     * @param builder     a builder with at least body set
     * @param t           the throwable responsible, if any
     * @param description the description, if any
     */
    CompletableFuture<RollbarResponse> log(Data.Builder builder, @Nullable Throwable t, @Nullable String description);
}
