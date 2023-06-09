package com.maynormoe.takeout.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/*
  @author Maynormoe
 */

/**
 * 通用返回结果
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Results<T> {
    private Integer code;
    private T data;
    private String msg;
    private Map map =new HashMap<>(); // 动态数据

    public static <T> Results<T> success(T object) {
        Results<T> results = new Results<T>();
        results.data = object;
        results.code = 1;
        results.msg = "请求成功";
        return results;
    }

    public static <T> Results<T> error(String message) {
        Results<T> results = new Results<T>();
        results.msg = message;
        results.data = null;
        results.code = 0;
        return results;
    }

    public Results<T> add(String key, Object value) {
        return (Results<T>) this.map.put(key, value);
    }
}
