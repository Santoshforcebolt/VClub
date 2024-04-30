package com.easyder.club;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.easyder.club.module.common.vo.CardBean;
import com.easyder.club.module.common.vo.CustomerBean;
import com.sky.wrapper.utils.PreferenceUtils;

import it.sauronsoftware.base64.Base64;

/**
 * Author: sky on 2020/5/19 21:54.
 * Email:xcode126@126.com
 * Desc:程序帮助类
 */
public class Helper {

    public static final String BASIC_USER = "CustomerBean"; //用户信息
    public static final String BASIC_TEL = "tel";
    public static final String BASIC_CID = "cid";
    public static final String BASIC_SEARCH = "search";//历史搜索内容 用“,”号隔开
    public static final String BASIC_CARD = "CardBean";//缓存信用卡号

    /**
     * 保存会员信息
     *
     * @param info
     */
    public static void saveCustomerInfo(CustomerBean info) {
        try {
            if (info != null) {
                String result = encode(JSONObject.toJSONString(info));
                PreferenceUtils.putPreference(App.getInstance(), BASIC_USER, result);
            } else {
                PreferenceUtils.putPreference(App.getInstance(), BASIC_USER, null);
            }
        } catch (Exception e) {

        }
    }

    /**
     * 获取会员信息
     *
     * @return
     */
    public static CustomerBean getCustomerInfo() {
        String result = PreferenceUtils.getPreference(App.getInstance(), BASIC_USER, null);
        if (!TextUtils.isEmpty(result)) {
            return JSONObject.parseObject(decode(result), CustomerBean.class);
        }
        return null;
    }

    /**
     * save cid
     * @param cid
     */
    public static void saveCid(String cid) {
        PreferenceUtils.putPreference(App.getInstance(), BASIC_CID, cid);
    }

    /**
     * get cid
     * @return
     */
    public static String getCid(){
        return PreferenceUtils.getPreference(App.getInstance(),BASIC_CID,null);
    }

    /**
     * save tel
     * @param tel
     */
    public static void saveTel(String tel) {
        PreferenceUtils.putPreference(App.getInstance(), BASIC_TEL, tel);
    }

    /**
     * get tel
     * @return
     */
    public static String getTel(){
        return PreferenceUtils.getPreference(App.getInstance(),BASIC_TEL,null);
    }

    /**
     * 保存信用卡
     *
     * @param info
     */
    public static void saveCardInfo(CardBean info) {
        try {
            if (info != null) {
                String result = encode(JSONObject.toJSONString(info));
                PreferenceUtils.putPreference(App.getInstance(), BASIC_CARD, result);
            } else {
                PreferenceUtils.putPreference(App.getInstance(), BASIC_CARD, null);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 获取信用卡
     *
     * @return
     */
    public static CardBean getCardInfo() {
        String result = PreferenceUtils.getPreference(App.getInstance(), BASIC_CARD, null);
        if (!TextUtils.isEmpty(result)) {
            return JSONObject.parseObject(decode(result), CardBean.class);
        }
        return null;
    }

    /**
     * 编码字符串
     *
     * @param data
     * @return
     */
    public static String encode(String data) {
        return data == null ? null : Base64.encode(data);
    }

    /**
     * 解码字符串
     *
     * @param result
     * @return
     */
    public static String decode(String result) {
        return result == null ? null : Base64.decode(result);
    }
}
