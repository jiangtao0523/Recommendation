package com.personal.recommendation.utils;

import java.util.HashMap;

/**
 * 自定义HashMap
 * @param <K>
 * @param <V>
 */
public class CustomizedHashMap<K, V> extends HashMap<K, V> {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder("{");
        for (K key : this.keySet()) {
            toString.append("\"").append(key).append("\":").append(this.get(key)).append(",");
        }
        if (toString.toString().equals("{")) {
            toString = new StringBuilder("{}");
        } else {
            toString = new StringBuilder(toString.substring(0, toString.length() - 1) + "}");
        }
        return toString.toString();

    }

}

