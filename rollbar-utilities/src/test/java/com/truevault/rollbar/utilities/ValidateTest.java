package com.truevault.rollbar.utilities;

import org.junit.Test;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ValidateTest {
    @Test(expected = ArgumentNullException.class)
    public void testIsNotNullOrWhitespaceThrowsForEmpty() throws Exception {
        Validate.isNotNullOrWhitespace("", "argName");
    }

    @Test(expected = ArgumentNullException.class)
    public void testIsNotNullOrWhitespaceThrowsForNull() throws Exception {
        Validate.isNotNullOrWhitespace(null, "argName");
    }

    @Test(expected = ArgumentNullException.class)
    public void testIsNotNullOrWhitespaceThrowsForWhitespace() throws Exception {
        Validate.isNotNullOrWhitespace("  \t\n", "argName");
    }

    @Test
    public void testIsNotNullOrWhitespaceUsesArgName() throws Exception {
        try {
            Validate.isNotNullOrWhitespace("  \t\n", "argName");
            fail();
        } catch (ArgumentNullException e) {
            String msg = e.getMessage();
            assertTrue(String.format("'%s' does not contain 'argName'", msg),
                    msg != null && msg.contains("argName"));
        }
    }

    @Test(expected = InvalidLengthException.class)
    public void testMaxLength() throws Exception {
        Validate.maxLength("Test", 2, "argName");
    }

    @Test
    public void testMaxLengthException() throws Exception {
        try {
            Validate.maxLength("Test", 2, "argName");
            fail();
        } catch (InvalidLengthException e) {
            String msg = e.getMessage();
            assertTrue(String.format("'%s' does not contain 'over 2'", msg),
                    msg != null && msg.contains("over 2"));
            assertTrue(String.format("'%s' does not contain 'argName'", msg), msg.contains("argName"));
        }
    }

    @Test(expected = InvalidLengthException.class)
    public void testMinLength() throws Exception {
        Validate.minLength(emptyList(), 1, "argName");
    }

    @Test
    public void testMinLengthException() throws Exception {
        try {
            Validate.minLength(emptyList(), 2, "argName");
            fail();
        } catch (InvalidLengthException e) {
            String msg = e.getMessage();
            assertTrue(String.format("'%s' does not contain 'under 2'", msg),
                    msg != null && msg.contains("under 2"));
            assertTrue(String.format("'%s' does not contain 'argName'", msg), msg.contains("argName"));
        }
    }

    @Test(expected = ArgumentNullException.class)
    public void testIsNotNull() throws Exception {
        Validate.isNotNull(null, "argName");
    }

    @Test
    public void testIsNotNullUsesArgName() throws Exception {
        try {
            Validate.isNotNull(null, "argName");
            fail();
        } catch (ArgumentNullException e) {
            String msg = e.getMessage();
            assertTrue(String.format("'%s' does not contain 'argName'", msg),
                    msg != null && msg.contains("argName"));
        }
    }
}
