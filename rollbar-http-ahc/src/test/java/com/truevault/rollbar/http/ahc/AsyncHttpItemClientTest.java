package com.truevault.rollbar.http.ahc;

import com.google.common.io.BaseEncoding;
import com.truevault.rollbar.http.ErrorMessageResponseException;
import com.truevault.rollbar.http.HttpResponseException;
import com.truevault.rollbar.http.RollbarResponse;
import com.truevault.rollbar.payload.Item;
import com.truevault.rollbar.payload.data.Data;
import com.truevault.rollbar.payload.data.Notifier;
import com.truevault.rollbar.payload.data.body.Body;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AsyncHttpItemClientTest {
    private final AsyncHttpItemClient sender;

    public AsyncHttpItemClientTest() {
        sender = new AsyncHttpItemClient();
    }

    @Test
    public void sendInvalidWorks() throws InterruptedException {
        CompletableFuture<RollbarResponse>
                response = sender.send(new Item("BAD_ACCESS_TOKEN",
                new Data.Builder().environment("test").body(Body.fromString("shouldn't work")).build()));

        try {
            response.get();
            fail();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            assertTrue(cause.toString(), cause instanceof ErrorMessageResponseException);
            ErrorMessageResponseException responseException = (ErrorMessageResponseException) cause;
            assertEquals("invalid access token", responseException.getErrorMessage());
            assertEquals(401, responseException.getHttpStatusCode());
        }
    }

    @Test
    public void sendValidWorks() throws ExecutionException, InterruptedException {
        Item p = new Item("e3a49f757f86465097c000cb2de9de08",
                new Data.Builder().environment("test").body(Body.fromString("hello")).build());
        p = new Item(p.accessToken(), p.data().toBuilder().notifier(new Notifier()).build());
        RollbarResponse response = sender.send(p).get();
        assertNotNull(response.getUuid());
    }

    @Test
    public void uuidDecodeRandomRoundtrip() {
        ByteBuffer buf = ByteBuffer.allocate(16);

        for (int i = 0; i < 1000; i++) {
            buf.clear();

            UUID u = UUID.randomUUID();
            buf.putLong(u.getMostSignificantBits());
            buf.putLong(u.getLeastSignificantBits());

            buf.flip();

            String hex = BaseEncoding.base16().encode(buf.array());

            UUID parsed = AsyncHttpItemClient.parseUUID(hex);

            assertEquals(u, parsed);
        }
    }

    @Test
    public void decodeSuccessResponse() throws IOException, HttpResponseException {
        RollbarResponse response = AsyncHttpItemClient.readResponse("{\n" +
                "  \"err\": 0,\n" +
                "  \"result\": {\n" +
                "    \"id\": null,\n" +
                "    \"uuid\": \"66e675f0b37f4a45ae9696359aa13700\"\n" +
                "  }\n" +
                "}", 200);

        assertEquals(UUID.fromString("66e675f0-b37f-4a45-ae96-96359aa13700"), response.getUuid());
    }

    @Test
    public void decodeGarbledSuccessResponseThrows() throws IOException, HttpResponseException {
        try {
            AsyncHttpItemClient.readResponse("{}", 200);
            fail();
        } catch (HttpResponseException e) {
            assertEquals("Could not parse successful response: <{}>", e.getMessage());
        }
    }

    @Test
    public void decodeErrorResponse() throws IOException, HttpResponseException {
        try {
            AsyncHttpItemClient.readResponse("{\n" +
                    "  \"err\": 1,\n" +
                    "  \"message\": \"invalid access token\"\n" +
                    "}", 401);
            fail();
        } catch (ErrorMessageResponseException e) {
            assertEquals("invalid access token", e.getErrorMessage());
        }
    }

    @Test
    public void decodeGarbledErrorResponse() throws IOException, HttpResponseException {
        try {
            AsyncHttpItemClient.readResponse("{}", 401);
            fail();
        } catch (HttpResponseException e) {
            assertEquals("Could not parse unsuccessful response: <{}>", e.getMessage());
        }
    }
}
