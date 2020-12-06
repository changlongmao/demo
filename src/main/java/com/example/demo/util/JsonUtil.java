package com.example.demo.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {


    public static <T> String getBeanJson(T t){
        Gson gson = new GsonBuilder().serializeNulls().create();
        return gson.toJson(t);
   }


}
