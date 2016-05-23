package com.truevault.rollbar.payload.data.body;

import com.truevault.rollbar.testing.GetAndSet;
import com.truevault.rollbar.testing.TestThat;
import com.truevault.rollbar.utilities.ArgumentNullException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class CrashReportTest {
    CrashReport report;

    @Before
    public void setUp() throws Exception {
        report = new CrashReport("HI");
    }

    @Test
    public void testRaw() throws Exception {
        TestThat.getAndSetWorks(report, "OOPS", "You broke it", new GetAndSet<CrashReport, String>() {
            public String get(CrashReport crashReport) {
                return crashReport.raw();
            }

            public CrashReport set(CrashReport crashReport, String val) {
                try {
                    return crashReport.raw(val);
                } catch (ArgumentNullException e) {
                    fail("Neither is null");
                }
                return null;
            }
        });
    }
}
