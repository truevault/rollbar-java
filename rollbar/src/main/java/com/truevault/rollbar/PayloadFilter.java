package com.truevault.rollbar;

import com.truevault.rollbar.payload.Payload;

public interface PayloadFilter {
    boolean shouldSend(Payload p, Throwable error, String message, Object... objects);
}
