package com.truevault.rollbar;

import com.truevault.rollbar.payload.Item;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ItemFilter {
    /**
     * Determine if an Item should be reported to Rollbar.
     *
     * @param item    the item
     * @param t       the associated throwable, if any
     * @param message the associated message, if any
     * @return true if the item should be reported to Rollbar
     */
    boolean shouldSend(@Nonnull Item item, @Nullable Throwable t, @Nullable String message);
}
