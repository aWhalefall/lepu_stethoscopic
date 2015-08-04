package com.lepu.stethoscopic.utils;

import com.google.gson.Gson;

/**
 * Created by weichyang on 2015/4/24.
 */
public class JsonUtils {

    public static <T> T json2Bean(String result, Class<T> clz) {
        Gson gson = new Gson();
        T t = gson.fromJson(result, clz);
        return t;
    }
}
