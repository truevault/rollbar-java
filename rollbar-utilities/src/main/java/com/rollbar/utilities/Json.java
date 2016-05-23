package com.rollbar.utilities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationConfig;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class Json {

    public static ObjectWriter getObjectWriter() {
        return Holder.WRITER;
    }

    public static ObjectReader getObjectReader() {
        return Holder.READER;
    }

    private static class Holder {

        private static final ObjectWriter WRITER;
        private static final ObjectReader READER;

        static {
            ObjectMapper mapper = new ObjectMapper();
            SerializationConfig config =
                    mapper.getSerializationConfig().withPropertyInclusion(JsonInclude.Value.construct(NON_NULL,
                            JsonInclude.Include.USE_DEFAULTS));

            mapper.setConfig(config);

            WRITER = mapper.writer();
            READER = mapper.reader();
        }
    }
}
