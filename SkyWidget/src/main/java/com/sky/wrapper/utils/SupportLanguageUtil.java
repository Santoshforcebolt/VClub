package com.sky.wrapper.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.LocaleList;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Author: sky on 2020/7/17 10:46.
 * Email:xcode126@126.com
 * Desc:
 */
public class SupportLanguageUtil {
    private static Map<String, Locale> mSupportLanguages = new HashMap<String, Locale>(7) {{
        put(LanguageUtils.LANG_VALUE_SIMPLE_CHINESE, Locale.SIMPLIFIED_CHINESE);
        put(LanguageUtils.LANG_VALUE_ENGLISH, Locale.ENGLISH);
//        put(LanguageConstants.TRADITIONAL_CHINESE, Locale.TRADITIONAL_CHINESE);
//        put(LanguageConstants.FRANCE, Locale.FRANCE);
//        put(LanguageConstants.GERMAN, Locale.GERMANY);
//        put(LanguageConstants.HINDI, new Locale(LanguageConstants.HINDI, "IN"));
//        put(LanguageConstants.ITALIAN, Locale.ITALY);
//        put(LanguageConstants.JAPAN, Locale.JAPAN);
    }};

    /**
     * 是否支持此语言
     *
     * @param language language
     * @return true:支持 false:不支持
     */
    public static boolean isSupportLanguage(String language) {
        return mSupportLanguages.containsKey(language);
    }

    /**
     * 获取支持语言
     *
     * @param language language
     * @return 支持返回支持语言，不支持返回系统首选语言
     */
    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSupportLanguage(String language) {
        if (isSupportLanguage(language)) {
            return mSupportLanguages.get(language);
        }
        return getSystemPreferredLanguage();
    }

    /**
     * 获取系统首选语言
     *
     * @return Locale
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Locale getSystemPreferredLanguage() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        return locale;
    }
}
