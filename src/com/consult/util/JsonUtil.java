package com.consult.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * Json-lib的封装类，实现json字符串与json对象之间的转换
 * 网友johncon提供稍作修改
 * 
 *
 */
public class JsonUtil {
    private static Logger LOG = LoggerFactory.getLogger(JsonUtil.class);
    
    /**
     * 字符串是否非空
     * 
     * @param str
     *            str
     * @return boolean
     */
    public static final boolean isNotBlank(Object str) {
        return !isBlank(str);
    }
    
    /**
     * 字符串是否为空
     * 
     * @param str
     *            str
     * @return boolean
     */
    public static final boolean isBlank(Object str) {
        if (str != null) {
            String s = str.toString();
            return "".equals(s) || "{}".equals(s) || "[]".equals(s)
                    || "null".equals(s);
        }
        else {
            return true;
        }
    }
    
    /**
     * 判断List是否为null或者空
     * 
     * @param str
     * @return
     */
    public static boolean isListEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }
    
    /** 
     * 判断是否是json结构 
     */
    public static boolean isJson(String value) {
        try {
            JSONObject.parseObject(value);
        }
        catch (JSONException e) {
            return false;
        }
        return true;
    }
    
    /** 
     * 判断是否是json结构 
     */
    public static boolean isJsonArray(String value) {
        try {
            JSONArray.parseObject(value);
        }
        catch (JSONException e) {
            return false;
        }
        return true;
    }
    
    /**
     * 对象是否是数组
     * @param obj
     * @return
     */
    private static boolean isArray(Object obj) {
        return obj instanceof Collection || obj.getClass().isArray();
    }
    
    public static String getStringNX(JSONObject jsonO, String key) {
        if (jsonO.containsKey(key)) {
            return jsonO.getString(key);
        }
        return "";
    }
    
    /**
     * 从JSONObject获取Long值
     * 
     * @param jsonObj
     * @param key
     */
    public static Long getLongFromJSON(JSONObject jsonObj, String key) {
        if (jsonObj == null) {
            return null;
        }
        if (!jsonObj.containsKey(key) || "".equals(jsonObj.getString(key))
                || "null".equals(jsonObj.getString(key))) {
            return null;
        }
        return jsonObj.getLong(key);
    }
    
    /**
     * 从JSONObject获取String值
     * 
     * @param jsonObj
     * @param key
     */
    public static String getStringFromJSON(JSONObject jsonObj, String key) {
        if (jsonObj == null) {
            return null;
        }
        if (!jsonObj.containsKey(key)) {
            return null;
        }
        String s = jsonObj.getString(key);
        if ("null".equals(s) || "".equals(s)) {
            s = null;
        }
        return s;
    }
    
    /**
     * 从JSONObject获取Date值
     * 
     * @param jsonObj
     * @param key
     */
    public static Date getDateFromJSON(JSONObject jsonObj, String key) {
        if (jsonObj == null) {
            return null;
        }
        if (!jsonObj.containsKey(key)) {
            return null;
        }
        String s = jsonObj.getString(key);
        if ("null".equals(s) || "".equals(s)) {
            return null;
        }
        else {
            try {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s);
            }
            catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
}
