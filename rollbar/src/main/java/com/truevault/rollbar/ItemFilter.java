package com.truevault.rollbar;

import com.truevault.rollbar.payload.Item;

public interface ItemFilter {
    boolean shouldSend(Item p, Throwable error, String message, Object... objects);
}
