package com.truevault.rollbar;

import com.truevault.rollbar.payload.Item;

public interface ItemTransform {
    Item transform(Item p, Throwable error, String message);
}
