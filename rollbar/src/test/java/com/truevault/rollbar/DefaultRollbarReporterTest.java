package com.truevault.rollbar;

import com.truevault.rollbar.http.ahc.AsyncHttpItemClient;
import java.util.concurrent.ExecutionException;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class DefaultRollbarReporterTest {

    @Test
    public void itDoesNotThrowANullPointerExceptionWhenLoggingAnException() throws ExecutionException,
            InterruptedException {
        DefaultRollbarReporter rollbar = new DefaultRollbarReporter.Builder(new AsyncHttpItemClient(), "foo", "e3a49f757f86465097c000cb2de9de08")
                .build();
        assertNotNull(rollbar.log(new Exception("some exception")).get().getUuid());
    }
}
