package com.truevault.rollbar.http;

import com.truevault.rollbar.payload.Item;
import java.util.concurrent.CompletableFuture;

/**
 * An abstraction around the underlying HTTP communication involved in sending an Item to Rollbar. See
 * https://rollbar.com/docs/api/items_post/ for what needs to be POSTed.
 */
public interface HttpItemClient {
    /**
     * Send an item (exception or log message) to Rollbar.
     *
     * This method will return immediately, and the communication will happen asynchronously. Use the returned future to
     * track the status of the request (e.g. if you wanted to block, call get()).
     *
     * If the request is unsuccessful, an exception will be available via the returned Future's normal {@link
     * java.util.concurrent.ExecutionException} mechanism. If an error message was available in the http response, an
     * {@link ErrorMessageResponseException} will be thrown. General issues with the HTTP response will result in a
     * {@link HttpResponseException}. I/O issues will result in an {@link java.io.IOException}.  Most usages will only
     * care about if it was successful, though, and not about what particular type of failure was encountered.
     *
     * @param item the item to send
     * @return a CompletableFuture of {@link RollbarResponse}.
     */
    CompletableFuture<RollbarResponse> send(Item item);
}
