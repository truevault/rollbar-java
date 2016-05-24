package com.truevault.rollbar.payload.data.body;

import java.util.LinkedHashMap;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BodyTest {

    @Test
    public void testFromError() throws Exception {
        Throwable err = getError();
        Body fromErr = Body.fromThrowable(err);
        assertNotNull(fromErr);
        assertNotNull(fromErr.trace());
        assertNotNull(fromErr.trace().exception());
        assertEquals("RuntimeException", fromErr.trace().exception().className());
        assertEquals("TRICKY!", fromErr.trace().exception().message());
        assertTrue(fromErr.trace().frames().size() > 2);
    }

    @Test
    public void testTraceChain() throws Exception {
        Throwable err = getChainedError();
        Body fromErr = Body.fromThrowable(err);
        assertNotNull(fromErr);
        assertNotNull(fromErr.traceChain());
        assertNotNull(fromErr.traceChain().traces());
        assertEquals(2, fromErr.traceChain().traces().length);
        assertEquals("RuntimeException", fromErr.traceChain().traces()[1].exception().className());
        assertEquals("TRICKY!", fromErr.traceChain().traces()[1].exception().message());
        assertEquals("IllegalStateException", fromErr.traceChain().traces()[0].exception().className());
        assertEquals("Nested Tricky!", fromErr.traceChain().traces()[0].exception().message());
    }

    @Test
    public void testMessage() throws Exception {
        Body fromStr = Body.fromString("Send a message");
        assertNotNull(fromStr);
        assertNotNull(fromStr.message());
        assertEquals("Send a message", fromStr.message().body());
    }

    @Test
    public void testMessageWithExtras() throws Exception {
        LinkedHashMap<String, Object> extras = new LinkedHashMap<>();
        extras.put("HELLO", "WORLD");
        Body fromStr = Body.fromString("Send a message", extras);
        assertNotNull(fromStr);
        assertNotNull(fromStr.message());
        assertEquals("Send a message", fromStr.message().body());
        assertEquals("WORLD", fromStr.message().get("HELLO"));
    }

    @Test
    public void testCrashReport() throws Exception {
        final String raw = "A CRASH REPORT WOULD BE WAY MORE COMPLICATED THAN THIS";
        Body crashed = Body.fromCrashReportString(raw);
        assertNotNull(crashed);
        assertNotNull(crashed.crashReport());
        assertEquals(raw, crashed.crashReport().raw());
    }

    @Test
    public void testContents() throws Exception {
        Body body = Body.fromString("Send a message");
        assertEquals(body.contents(), body.message());
        assertNull(body.trace());
        assertNull(body.traceChain());
        assertNull(body.crashReport());

        body = body.contents(TraceChain.fromThrowable(getError()));
        assertEquals(body.contents(), body.traceChain());
        assertNull(body.message());
        assertNull(body.trace());
        assertNull(body.crashReport());

        body = body.contents(Trace.fromThrowable(getChainedError()));
        assertEquals(body.contents(), body.trace());
        assertNull(body.message());
        assertNull(body.traceChain());
        assertNull(body.crashReport());

        body = body.contents(new CrashReport("OOPS"));
        assertEquals(body.contents(), body.crashReport());
        assertNull(body.message());
        assertNull(body.trace());
        assertNull(body.traceChain());
    }

    private Throwable getError() {
        try {
            causeError();
        } catch (Throwable t) {
            return t;
        }
        return null;
    }

    private void causeError() {
        throw new RuntimeException("TRICKY!");
    }

    private void causeChainedError() {
        try {
            causeError();
        } catch (Throwable t) {
            throw new IllegalStateException("Nested Tricky!", t);
        }
    }

    private Throwable getChainedError() {
        try {
            causeChainedError();
        } catch (Throwable t) {
            return t;
        }
        return null;
    }
}
