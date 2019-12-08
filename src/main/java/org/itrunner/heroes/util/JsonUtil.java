package org.itrunner.heroes.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public final class JsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();

    private JsonUtil() {
    }

    /**
     * Serialize any Java value as a String.
     */
    public static String asJson(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    /**
     * Deserialize JSON content from given JSON content String.
     */
    public static <T> T parseJson(String content, Class<T> valueType) throws IOException {
        return mapper.readValue(content, valueType);
    }
}