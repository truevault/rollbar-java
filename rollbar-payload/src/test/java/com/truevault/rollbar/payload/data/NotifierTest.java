package com.truevault.rollbar.payload.data;

import com.truevault.rollbar.testing.GetAndSet;
import com.truevault.rollbar.testing.TestThat;
import org.junit.Before;
import org.junit.Test;

public class NotifierTest {

    private Notifier notifier;

    @Before
    public void setUp() throws Exception {
        notifier = new Notifier("RollbarJava", "alpha");
    }

    @Test
    public void testName() throws Exception {
        TestThat.getAndSetWorks(notifier, "name-one", "name-two", new GetAndSet<Notifier, String>() {
            public String get(Notifier item) {
                return item.name();
            }

            public Notifier set(Notifier item, String val) {
                return item.name(val);
            }
        });
    }

    @Test
    public void testVersion() throws Exception {
        TestThat.getAndSetWorks(notifier, "ecba12", "SHA-15f0a", new GetAndSet<Notifier, String>() {
            public String get(Notifier item) {
                return item.version();
            }

            public Notifier set(Notifier item, String val) {
                return item.version(val);
            }
        });
    }
}
