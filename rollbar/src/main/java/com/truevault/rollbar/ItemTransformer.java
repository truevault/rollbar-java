package com.truevault.rollbar;

import com.truevault.rollbar.payload.Item;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ItemTransformer {
    /**
     * Transform an item about to be sent to Rollbar (e.g. to scrub passwords or something like that).
     *
     * @param item      the original item
     * @param throwable the throwable, if any
     * @param message   the message, if any
     * @return an item
     */
    @Nonnull
    Item transform(@Nonnull Item item, @Nullable Throwable throwable, String message);
}
