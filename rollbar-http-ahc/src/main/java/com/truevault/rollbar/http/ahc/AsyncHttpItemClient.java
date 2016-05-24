package com.truevault.rollbar.http.ahc;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.truevault.rollbar.http.ErrorMessageResponseException;
import com.truevault.rollbar.http.HttpItemClient;
import com.truevault.rollbar.http.HttpResponseException;
import com.truevault.rollbar.http.RollbarResponse;
import com.truevault.rollbar.payload.Item;
import com.truevault.rollbar.utilities.ArgumentNullException;
import com.truevault.rollbar.utilities.Json;
import com.truevault.rollbar.utilities.Validate;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nonnull;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * A HttpItemClient implementation that uses Async Http Client (https://github.com/AsyncHttpClient/async-http-client).
 */
public class AsyncHttpItemClient implements HttpItemClient {
    /**
     * If you don't set the url this is the URL that gets used.
     */
    public static final String DEFAULT_API_ENDPOINT = "https://api.rollbar.com/api/1/item/";

    private static final JsonPointer SUCCESS_UUID_PTR = JsonPointer.compile("/result/uuid");
    private static final JsonPointer ERROR_MSG_PTR = JsonPointer.compile("/message");

    private final String url;
    private final AsyncHttpClient httpClient;

    /**
     * Default constructor, sends to the public api endpoint.
     *
     * @throws ArgumentNullException if url is null
     */
    public AsyncHttpItemClient() {
        this(DEFAULT_API_ENDPOINT);
    }

    /**
     * @param url The Rollbar endpoint to POST items to.
     */
    public AsyncHttpItemClient(@Nonnull String url) {
        this(url, new DefaultAsyncHttpClient());
    }

    public AsyncHttpItemClient(@Nonnull String url, @Nonnull AsyncHttpClient httpClient) {
        Validate.isNotNull(url, "url");
        this.url = url;
        this.httpClient = httpClient;
    }

    /**
     * Sends the json (rollbar payload) to the endpoint configured in the constructor. Returns the (parsed) response
     * from Rollbar.
     *
     * @param item the serialized JSON payload
     * @return the response from Rollbar {@link RollbarResponse}
     */
    @Override
    public CompletableFuture<RollbarResponse> send(Item item) {
        CompletableFuture<RollbarResponse> cf = new CompletableFuture<>();

        final byte[] bytes;
        try {
            bytes = Json.getObjectWriter().writeValueAsBytes(item);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not serialize json", e);
        }

        httpClient.preparePost(url)
                .setBody(bytes)
                .addHeader("Accept-Charset", "utf-8")
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .execute(new AsyncCompletionHandler<Void>() {
                    @Override
                    public Void onCompleted(Response response) throws IOException, HttpResponseException {
                        RollbarResponse rollbarResponse =
                                readResponse(response.getResponseBody(UTF_8), response.getStatusCode());
                        cf.complete(rollbarResponse);
                        return null;
                    }

                    @Override
                    public void onThrowable(Throwable t) {
                        cf.completeExceptionally(t);
                    }
                });

        return cf;
    }

    @Nonnull
    static RollbarResponse readResponse(String body, int statusCode) throws HttpResponseException, IOException {
        JsonNode node = Json.getObjectReader().readTree(body);

        if (statusCode == 200) {
            String uuidStr = node.at(SUCCESS_UUID_PTR).asText("");

            if (uuidStr.length() == 32) {
                return RollbarResponse.ok(parseUUID(uuidStr));
            }

            throw new HttpResponseException(statusCode,
                    "Could not parse successful response: <" + body + ">");
        }

        JsonNode errorMsgNode = node.at(ERROR_MSG_PTR);
        if (errorMsgNode.isMissingNode()) {
            throw new HttpResponseException(statusCode,
                    "Could not parse unsuccessful response: <" + body + ">");
        }

        throw new ErrorMessageResponseException(statusCode, errorMsgNode.asText(""));
    }

    /**
     * @param uuidStr a string of 32 hex bytes, most significant bits first
     * @return a UUID
     */
    @Nonnull
    static UUID parseUUID(String uuidStr) {

        // parse in 4-byte chunks to avoid max value quirks
        long mostSigBits = Long.parseUnsignedLong(uuidStr.substring(0, 8), 16);
        mostSigBits <<= 32;
        mostSigBits |= Long.parseUnsignedLong(uuidStr.substring(8, 16), 16);
        long leastSigBits = Long.parseUnsignedLong(uuidStr.substring(16, 24), 16);
        leastSigBits <<= 32;
        leastSigBits |= Long.parseUnsignedLong(uuidStr.substring(24, 32), 16);

        return new UUID(mostSigBits, leastSigBits);
    }
}
