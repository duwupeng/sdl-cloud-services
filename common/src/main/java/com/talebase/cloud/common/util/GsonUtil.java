package com.talebase.cloud.common.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Created by eric.du on 2016-12-28.
 */
public class GsonUtil {
        private static class SingletonGson {
            private static final Gson INSTANCE = new Gson();
        }
    public static String toJson(Object obj) {
        return SingletonGson.INSTANCE.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        return SingletonGson.INSTANCE.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) throws JsonSyntaxException {
        return SingletonGson.INSTANCE.fromJson(json, typeOfT);
    }

}
