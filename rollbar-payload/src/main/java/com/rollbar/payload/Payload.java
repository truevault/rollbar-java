package com.rollbar.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rollbar.payload.data.Data;
import com.rollbar.payload.data.Level;
import com.rollbar.payload.data.Notifier;
import com.rollbar.payload.data.body.Body;
import com.rollbar.utilities.ArgumentNullException;
import com.rollbar.utilities.Validate;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Represents the payload to send to Rollbar. A successfully constructed Payload matches Rollbar's spec, and should be
 * successful when serialized and POSTed to the correct endpoint.
 */
public final class Payload {
    /**
     * A shortcut factory for creating a payload
     * @param accessToken not nullable, the server_post access token to send this payload to
     * @param environment not nullable, the environment the code is currently running under
     * @param error not nullable, the error being reported
     * @param custom any custom data to be sent (null is OK)
     * @return the payload
     */
    public static Payload fromError(String accessToken, String environment, Throwable error, LinkedHashMap<String, Object> custom) {
        Validate.isNotNullOrWhitespace(accessToken, "accessToken");
        Validate.isNotNullOrWhitespace(environment, "environment");
        Validate.isNotNull(error, "error");

        Body body = Body.fromError(error);
        Level level = error instanceof Error ? Level.CRITICAL : Level.ERROR;
        String platform = System.getProperty("java.version");
        Data d = new Data(environment, body, level, new Date(), null, platform, "java", null, null, null, null, null, custom, null, null, null, new Notifier());
        return new Payload(accessToken, d);
    }

    /**
     * A shortcut factory for creating a payload
     * @param accessToken not nullable, the server_post access token to send this payload to
     * @param environment not nullable, the environment the code is currently running under
     * @param message not nullable, the message to log to Rollbar
     * @param custom any custom data to be sent (null is OK)
     * @return the payload
     */
    public static Payload fromMessage(String accessToken, String environment, String message, LinkedHashMap<String, Object> custom) {
        Validate.isNotNullOrWhitespace(accessToken, "accessToken");
        Validate.isNotNullOrWhitespace(environment, "environment");
        Validate.isNotNull(message, "message");

        Body body = Body.fromString(message, custom);
        String platform = System.getProperty("java.version");
        Data d = new Data(environment, body, Level.WARNING, new Date(), null, platform, "java", null, null, null, null, null, null, null, null, null, new Notifier());
        return new Payload(accessToken, d);
    }

    private final String accessToken;
    private final Data data;

    /**
     * Constructor
     * @param accessToken An access token with scope "post_server_item" or "post_client_item". Probably "server" unless
     *                    your {@link Data#platform()} is "android" or "client". Must not be null or whitespace.
     * @param data The data to POST to Rollbar. Must not be null.
     * @throws ArgumentNullException if either argument was null
     */
    public Payload(String accessToken, Data data) throws ArgumentNullException {
        Validate.isNotNullOrWhitespace(accessToken, "accessToken");
        this.accessToken = accessToken;

        Validate.isNotNull(data, "data");
        this.data = data;
    }

    /**
     * @return the access token
     */
    @JsonProperty("access_token")
    public String accessToken() {
        return accessToken;
    }

    /**
     * @return the data
     */
    @JsonProperty("data")
    public Data data() {
        return data;
    }

    /**
     * Set the access token
     * @param token the new access token
     * @return a copy of this Payload with the token overridden
     * @throws ArgumentNullException if {@code token} is null
     */
    public Payload accessToken(String token) throws ArgumentNullException {
        return new Payload(token, this.data);
    }

    /**
     * Set the data
     * @param data the new data
     * @return a copy of this Payload with the data overridden
     * @throws ArgumentNullException if {@code data} is null
     */
    public Payload data(Data data) throws ArgumentNullException {
        return new Payload(this.accessToken, data);
    }

}
