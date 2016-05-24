package com.truevault.rollbar.http;

import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The RollbarResponse is the response received from Rollbar after sending a report.
 */
public class RollbarResponse {
    @Nullable
    private final UUID uuid;

    @Nonnull
    private final ResponseType responseType;

    public static RollbarResponse ok(@Nonnull UUID uuid) {
        return new RollbarResponse(uuid, ResponseType.OK);
    }

    public static RollbarResponse filtered() {
        return new RollbarResponse(null, ResponseType.FILTERED);
    }

    private RollbarResponse(@Nullable UUID uuid, @Nonnull ResponseType responseType) {
        this.uuid = uuid;
        this.responseType = responseType;
    }

    /**
     * @return if response type is OK, the getUuid of the occurrence that happened because of the request, else null,
     */
    @Nullable
    public UUID getUuid() {
        return uuid;
    }

    @Nonnull
    public ResponseType getResponseType() {
        return responseType;
    }

    enum ResponseType {
        /**
         * The request succeeded
         */
        OK,
        /**
         * The request was not allowed by the configured ItemFilter
         */
        FILTERED
    }
}
