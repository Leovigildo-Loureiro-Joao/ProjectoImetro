package com.imetro.domain;

import java.util.HashMap;

public class Cache {
    public static HashMap<String,Object> cache=new HashMap<>();

    public static void put(String key,Object value){
        cache.put(key, value);
    }

    public static Object get(String key){
        return cache.get(key);
    }

    public static void remove(String key){
        cache.remove(key);
    }
}
