package com.personal.recommendation.utils;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Json处理工具类
 */
public class JsonUtil {

    /**
     * 获取用户所关注的模板的id的set
     */
    @SuppressWarnings("unused")
    public static Set<Integer> getUserModuleIdSet(String srcJson) {

        // java的擦除机制不允许直接获取泛型类的class,但是这样会使得jackson的readValue自动将键转换为String，于是需要使用jackson提供的TypeReference来解决这个问题
        Map<Integer, Object> map = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            map = objectMapper.readValue(srcJson, new TypeReference<Map<Integer, Object>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Objects.requireNonNull(map).keySet();
    }

    /**
     * 获得用户对应module的喜好关键词列表的map
     */
    @SuppressWarnings({"unchecked","unused"})
    public static LinkedHashMap<String, Double> getModulePrefMap(String srcJson, int moduleId) {

        LinkedHashMap<String, Double> keyWordsRateMap = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // java的擦除机制不允许直接获取泛型类的class,但是这样会使得jackson的readValue自动将键转换为String，于是需要使用jackson提供的TypeReference来解决这个问题
            Map<Integer, Object> map = objectMapper.readValue(srcJson, new TypeReference<Map<Integer, Object>>() {
            });
            keyWordsRateMap = (LinkedHashMap<String, Double>) map.get(moduleId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keyWordsRateMap;
    }

    /**
     * 将用户的喜好关键词列表字符串转换为map
     */
    public static CustomizedHashMap<Integer, CustomizedHashMap<String, Double>> jsonPrefListToMap(String srcJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        CustomizedHashMap<Integer, CustomizedHashMap<String, Double>> map = null;
        try {
            map = objectMapper.readValue(srcJson, new TypeReference<CustomizedHashMap<Integer, CustomizedHashMap<String, Double>>>() {
            });
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

}
