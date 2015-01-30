package org.egov.search.util;

import java.io.IOException;
import java.io.StringWriter;

public class Serializer {

    public static String toJson(Object objectToSerialize) {
        StringWriter stringWriter = new StringWriter();
        try {
            new JsonMapper().writeValue(stringWriter, objectToSerialize);
        } catch (IOException e) {
            throw new RuntimeException("Error wile converting to JSON", e);
        }
        return stringWriter.toString();
    }

    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        T serializedObject;
        try {
            serializedObject = new JsonMapper().readValue(jsonString, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Error wile converting from JSON", e);
        }
        return serializedObject;
    }
}