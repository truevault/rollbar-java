package com.truevault.rollbar.payload.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.truevault.rollbar.payload.data.body.Body;
import com.truevault.rollbar.payload.data.body.CodeContext;
import com.truevault.rollbar.payload.data.body.ExceptionInfo;
import com.truevault.rollbar.payload.data.body.Frame;
import com.truevault.rollbar.payload.data.body.Trace;
import java.io.IOException;
import java.net.InetAddress;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.Test;

import static com.truevault.rollbar.utilities.Json.getObjectReader;
import static com.truevault.rollbar.utilities.Json.getObjectWriter;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class DataTest {

    @Test
    public void jsonWithMessageBody() throws IOException {
        Map<String, Object> messageExtra = new LinkedHashMap<>();
        messageExtra.put("str", "s");
        messageExtra.put("int", 3);
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("X-Foo", "bar");
        Map<String, String> params = new LinkedHashMap<>();
        params.put("p", "v");
        Map<String, String> get = new LinkedHashMap<>();
        get.put("a", "b");
        Map<String, Object> post = new LinkedHashMap<>();
        post.put("c", "d");
        Map<String, Object> requestExtra = new LinkedHashMap<>();
        requestExtra.put("req", "extra");
        Map<String, Object> serverExtra = new LinkedHashMap<>();
        serverExtra.put("server", "extra");
        Map<String, Object> dataCustom = new LinkedHashMap<>();
        dataCustom.put("data", "custom");
        Data data = new Data.Builder(Body.fromString("foo", messageExtra), "env")
                .level(Level.DEBUG)
                .timestamp(Instant.ofEpochSecond(1464219077))
                .codeVersion("codeVers")
                .platform("p")
                .language("l")
                .framework("f")
                .context("c")
                .request(new Request("url", "method", headers, params, get, "a=b", post, "body",
                        InetAddress.getLocalHost(), requestExtra))
                .person(new Person("id", "user", "foo@bar.com"))
                .server(new Server("localhost", "root", "branch", "vers", serverExtra))
                .custom(dataCustom)
                .fingerprint("fingerprint")
                .title("title")
                .uuid(UUID.fromString("49622cce-72ef-40da-a8ab-68e998d99981"))
                .notifier(new Notifier())
                .build();

        String json = getObjectWriter().writeValueAsString(data);

        assertJsonEquals(json, "dataWithMessage.json");
    }

    @Test
    public void jsonWithRawBody() throws IOException {
        Data data = new Data.Builder(Body.fromCrashReportString("mystery"), "env").build();
        String json = getObjectWriter().writeValueAsString(data);

        assertJsonEquals(json, "dataWithRaw.json");
    }

    @Test
    public void jsonWithSimpleTrace() throws IOException {
        Data data = new Data.Builder(Body.fromThrowable(new DummyStack(), "desc"), "test").build();

        String json = getObjectWriter().writeValueAsString(data);

        assertJsonEquals(json, "dataWithTrace.json");
    }

    @Test
    public void jsonWithTraceAndCause() throws IOException {
        Data data = new Data.Builder(Body.fromThrowable(new DummyStack2(new DummyStack()), "desc"), "test").build();

        String json = getObjectWriter().writeValueAsString(data);

        assertJsonEquals(json, "dataWithTraceAndCause.json");
    }

    @Test
    public void jsonWithManualTrace() throws IOException {
        CodeContext codeContext = new CodeContext(asList("pre1", "pre2"), asList("post1", "post2"));
        Map<String, Object> kwargs = new LinkedHashMap<>();
        kwargs.put("foo", "bar");
        Frame frame = new Frame("file", 1, 2, "method", "line of code", codeContext, asList("arg1", 2), kwargs);
        Data data = new Data.Builder(
                new Body(new Trace(asList(frame), new ExceptionInfo("klass", "message", "desc"))), "test")
                .build();

        String json = getObjectWriter().writeValueAsString(data);

        assertJsonEquals(json, "dataWithManualTrace.json");
    }

    private void assertJsonEquals(String actualStr, String pathToExpected) throws IOException {
        JsonNode actual = getObjectReader().readTree(actualStr);
        JsonNode expected = getObjectReader().readTree(getClass().getResourceAsStream(pathToExpected));
        assertEquals("Was " + getObjectWriter().withDefaultPrettyPrinter().writeValueAsString(actual),
                expected, actual);
    }
}
