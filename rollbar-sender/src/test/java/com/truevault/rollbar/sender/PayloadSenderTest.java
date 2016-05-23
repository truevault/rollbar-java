package com.truevault.rollbar.sender;

import com.truevault.rollbar.payload.Payload;
import com.truevault.rollbar.payload.data.Notifier;
import com.truevault.rollbar.utilities.ArgumentNullException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class PayloadSenderTest {
    private final PayloadSender sender;

    public PayloadSenderTest() throws ArgumentNullException {
        try {
            sender = new PayloadSender();
        } catch (ArgumentNullException e) {
            fail("This shouldn't happen since I didn't pass null to PayloadSender");
            throw e;
        }
    }

    @Test
    public void SendInvalidWorks() {
        RollbarResponse response = sender.send(Payload.fromMessage("BAD_ACCESS_TOKEN", "test", "test from rollbar-java", null));
        assertEquals(RollbarResponseCode.Unauthorized, response.statusCode());
        assertNotNull(response.errorMessage());
        assertNull(response.uuid());
    }

    @Test
    public void SendValidWorks() {
        Payload p = Payload.fromMessage("e3a49f757f86465097c000cb2de9de08", "Hello, World!", "production", null);
        p = p.data(p.data().notifier(new Notifier()));
        RollbarResponse response = sender.send(p);
        assertEquals(RollbarResponseCode.Success, response.statusCode());
        assertNotNull(response.uuid());
        assertNull(response.errorMessage());
    }
}
