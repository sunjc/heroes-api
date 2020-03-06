package org.itrunner.heroes.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtils {
    private static ObjectMapper mapper = new ObjectMapper();

    private JsonUtils() {
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
    public static <T> T parseJson(String content, Class<T> valueType) throws JsonProcessingException {
        return mapper.readValue(content, valueType);
    }
}