package com.truevault.rollbar.utilities;

import org.junit.Assert;
import org.junit.Test;

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
        } catch (ArgumentNullException e) {
            String msg = e.getMessage();
            Assert.assertTrue(String.format("'%s' does not contain 'argName'", msg), msg != null && msg.contains("argName"));
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
        } catch (InvalidLengthException e) {
            String msg = e.getMessage();
            Assert.assertTrue(String.format("'%s' does not contain 'over 2'", msg), msg != null && msg.contains("over 2"));
            Assert.assertTrue(String.format("'%s' does not contain 'argName'", msg), msg.contains("argName"));
        }
    }

    @Test(expected = InvalidLengthException.class)
    public void testMinLength() throws Exception {
        Validate.minLength(new String[] {}, 1, "argName");
    }

    @Test
    public void testMinLengthException() throws Exception {
        try {
            Validate.minLength(new Character[0], 2, "argName");
        } catch (InvalidLengthException e) {
            String msg = e.getMessage();
            Assert.assertTrue(String.format("'%s' does not contain 'under 2'", msg), msg != null && msg.contains("under 2"));
            Assert.assertTrue(String.format("'%s' does not contain 'argName'", msg), msg.contains("argName"));
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
        } catch (ArgumentNullException e) {
            String msg = e.getMessage();
            Assert.assertTrue(String.format("'%s' does not contain 'argName'", msg), msg != null && msg.contains("argName"));
        }
    }

}
