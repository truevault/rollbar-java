package com.truevault.rollbar.payload.data.body;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class MessageTest {
    Message m;

    @Before
    public void setUp() throws Exception {
        m = new Message("HELLO");
    }

    @Test
    public void testCopy() throws Exception {
        assertNotSame(m, m.copy());
        assertEquals(m.body(), m.copy().body());
    }

    @Test
    public void testBody() throws Exception {
        assertNotSame(m, m.body("Test"));
        assertSame(m.keys(true).size(), m.body("TEST").keys(true).size());
    }
}
