package com.truevault.rollbar.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.truevault.rollbar.payload.data.Data;
import com.truevault.rollbar.utilities.ArgumentNullException;
import com.truevault.rollbar.utilities.Validate;
import javax.annotation.Nonnull;

public final class Item {
    private final String accessToken;
    private final Data data;

    /**
     * @throws ArgumentNullException if either argument was null
     */
    public Item(@Nonnull String accessToken, @Nonnull Data data) throws ArgumentNullException {
        Validate.isNotNullOrWhitespace(accessToken, "accessToken");
        this.accessToken = accessToken;

        Validate.isNotNull(data, "data");
        this.data = data;
    }

    @JsonProperty("access_token")
    public String accessToken() {
        return accessToken;
    }

    @JsonProperty("data")
    public Data data() {
        return data;
    }
}
