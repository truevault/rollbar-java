package com.truevault.rollbar.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.truevault.rollbar.payload.data.Data;
import com.truevault.rollbar.payload.data.Notifier;
import com.truevault.rollbar.payload.data.body.Body;
import com.truevault.rollbar.payload.data.body.Message;
import java.io.IOException;
import java.util.LinkedHashMap;
import org.junit.Test;

import static com.truevault.rollbar.utilities.Json.getObjectReader;
import static com.truevault.rollbar.utilities.Json.getObjectWriter;
import static org.junit.Assert.assertEquals;

public class RollbarSerializerTest {
    private static final String accessToken = "e3a49f757f86465097c000cb2de9de08";
    private static final String environment = "testing";
    private static final String testMessage = "Test Serialize";
    private final static String basicExpected =
            "{\"access_token\":\"" + accessToken + "\",\"data\":{\"environment\":\"" + environment +
                    "\",\"body\":{\"message\":{\"body\":\"" + testMessage +
                    "\",\"extra\":\"has-extra\"}},\"notifier\":{\"name\":\"truevault-rollbar\"}}}";

    @Test
    public void testBasicSerialize() throws JsonProcessingException {
        final LinkedHashMap<String, Object> members = new LinkedHashMap<>();
        members.put("extra", "has-extra");
        final Body body = Body.fromString(testMessage, members);
        final Data data = new Data.Builder(body, environment)
                .notifier(new Notifier()).build();

        String json = getObjectWriter().writeValueAsString(new Item(accessToken, data));
        assertEquals(basicExpected, json);
    }

    @Test
    public void testExceptionSerialize() throws IOException {
        final Body body = Body.fromThrowable(getThrowable());
        final Data data = new Data.Builder(body, environment).build();
        String json = getObjectWriter().writeValueAsString(new Item(accessToken, data));
        ObjectNode parsed = getObjectReader().forType(ObjectNode.class).readValue(json);
        assertEquals(accessToken, parsed.get("access_token").textValue());
        assertEquals(environment, parsed.get("data").get("environment").textValue());
        assertEquals("Exception", parsed.get("data").get("body").get("trace")
                .get("exception").get("class").textValue());
        assertEquals("Non Chained Exception",
                parsed.get("data").get("body").get("trace")
                        .get("exception").get("message").textValue());

        final JsonNode frames = parsed.get("data").get("body").get("trace")
                .get("frames");
        final JsonNode lastFrame = frames.get(frames.size() - 1);
        final JsonNode secondToLastFrame = frames.get(frames.size() - 2);

        assertEquals("com.truevault.rollbar.payload.RollbarSerializerTest.java", lastFrame.get("filename").textValue());
        assertEquals("com.truevault.rollbar.payload.RollbarSerializerTest.java",
                secondToLastFrame.get("filename").textValue());
        assertEquals("throwException", lastFrame.get("method").textValue());
        assertEquals("getThrowable", secondToLastFrame.get("method").textValue());
    }

    @Test
    public void testChainedExceptionSerialize() throws IOException {
        final Body body = Body.fromThrowable(getChainedThrowable());
        final Data data = new Data.Builder(body, environment).build();
        String json = getObjectWriter().writeValueAsString(new Item(accessToken, data));
        ObjectNode parsed = getObjectReader().forType(ObjectNode.class).readValue(json);
        assertEquals(accessToken, parsed.get("access_token").textValue());
        assertEquals(environment, parsed.get("data").get("environment").textValue());
        final JsonNode b = parsed.get("data").get("body");
        assertEquals(2, b.get("trace_chain").size());
    }

    @Test
    public void testExtensibleSerialize() throws IOException {
        final Message msg = new Message("Message").put("extra", "value");
        final Body body = new Body(msg);
        final Data data = new Data.Builder(body, environment).build();
        String json = getObjectWriter().writeValueAsString(new Item(accessToken, data));
        JsonNode parsed = getObjectReader().forType(ObjectNode.class).readValue(json);
        final String b =
                parsed.get("data").get("body").get("message").get("extra")
                        .textValue();
        assertEquals("value", b);
    }

    public Throwable getThrowable() {
        try {
            throwException();
            return null;
        } catch (Throwable t) {
            return t;
        }
    }

    private void throwException() throws Exception {
        throw new Exception("Non Chained Exception");
    }

    public Throwable getChainedThrowable() {
        try {
            throwChainedException();
            return null;
        } catch (Throwable t) {
            return t;
        }
    }

    public void throwChainedException() throws Exception {
        try {
            throwException();
        } catch (Exception e) {
            throw new Exception("Wrapper Exception", e);
        }
    }
}
