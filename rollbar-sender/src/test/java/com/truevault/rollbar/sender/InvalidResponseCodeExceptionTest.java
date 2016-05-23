package com.truevault.rollbar.sender;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InvalidResponseCodeExceptionTest {
    @Test
    public void testValue() throws Exception {
        InvalidResponseCodeException e = new InvalidResponseCodeException(12);
        assertEquals(12, e.value());
        assertTrue(e.getMessage().contains("12"));
    }
}
