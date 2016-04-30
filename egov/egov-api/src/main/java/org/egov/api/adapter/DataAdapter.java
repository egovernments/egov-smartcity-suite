package org.egov.api.adapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class DataAdapter<T> implements JsonSerializer<T> {

    private T clazz = null;

    @SuppressWarnings("unchecked")
    public DataAdapter() {
        try {

            this.clazz = (T) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public Class<T> getBaseObject() {
        return (Class<T>) this.clazz;
    }

    public Type getTypeToken() {

        return new TypeToken<T>() {
        }.getType();
    }
}