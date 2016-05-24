package com.truevault.rollbar;

import com.truevault.rollbar.http.ahc.AsyncHttpItemClient;
import java.util.concurrent.ExecutionException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class RollbarTest {

    @Test
    public void itDoesNotThrowANullPointerExceptionWhenLoggingAnException() throws ExecutionException,
            InterruptedException {
        Rollbar rollbar =
                new Rollbar("e3a49f757f86465097c000cb2de9de08", "some-environment", new AsyncHttpItemClient());
        assertNotNull(rollbar.log(new Exception("some exception")).get().getUuid());
    }
}
