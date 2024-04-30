package com.easyder.club.utils;


import androidx.collection.ArrayMap;

import com.easyder.club.App;
import com.sky.wrapper.utils.LogUtils;

import java.io.Serializable;


/**
 * Auther:  winds
 * Data:    2017/4/19
 * Desc:
 */

public class RequestParams {
    ArrayMap<String, Serializable> params;

    public RequestParams() {
        this.params = new ArrayMap<>();
    }

    public RequestParams put(String key, Serializable value) {
        params.put(key, value);
        return this;
    }

    public RequestParams putCid() {
        if (App.isLogin()) {
            put("cid", App.getCid());
            LogUtils.i("syk", "cid==" + App.getCid());
        }
        return this;
    }

    /**
     * 去除null 空字符串 -1 加入集合
     *
     * @param key
     * @param value
     * @return
     */
    public RequestParams putWithoutEmpty(String key, Serializable value) {
        if (value != null) {
            if (value instanceof String) {
                if ((((String) value).trim().length() == 0)) {
                    return this;
                }
            }

            if (value instanceof Integer) {
                if (((Integer) value) == -1) {
                    return this;
                }
                if (((Integer) value) == 0) {
                    return this;
                }
            }
            params.put(key, value);
        }
        return this;
    }

    public Serializable get(String key) {
        return params.get(key);
    }

    public ArrayMap<String, Serializable> get() {
        LogUtils.info("--> " + params.toString());
        return params;
    }
}
