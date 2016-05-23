package com.truevault.rollbar;

import com.truevault.rollbar.payload.Payload;

public interface PayloadTransform {
    Payload transform(Payload p, Throwable error, String message);
}
