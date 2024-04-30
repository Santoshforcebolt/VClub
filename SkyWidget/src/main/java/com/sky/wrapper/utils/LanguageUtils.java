package com.sky.wrapper.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Author: sky on 2020/6/8 16:24.
 * Email:xcode126@126.com
 * Desc:operate utils
 */
public class LanguageUtils {

    //多语言   注意 不要改动  此处为后台定义好的字段
    public final static String LANG_NAME = "lang";
    public final static String LANG_VALUE_SIMPLE_CHINESE = "zh_CN";
    public final static String LANG_VALUE_ENGLISH = "en_US";
    public final static String LANG_VALUE_DEFAULT = LANG_VALUE_ENGLISH;

    /**
     * 获取服务器规定的语言格式
     *
     * @param context
     * @return
     */
    public static String getLangName(Context context) {
        return (LanguageUtils.getCurrentLanguage(context).contains("zh") ? "zh" : "en");
    }

    /**
     * 获取当前的语言状态是否是英文
     *
     * @param mContext
     * @return
     */
    public static boolean isEnglishLanguage(Context mContext) {
        String lang = getCurrentLanguage(mContext);
        if (!TextUtils.isEmpty(lang) && lang.equals(LanguageUtils.LANG_VALUE_ENGLISH)) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前语言环境，有默认值
     *
     * @param context
     * @return
     */
    public static String getCurrentLanguage(Context context) {
        return PreferenceUtils.getPreference(context, LANG_NAME, LANG_VALUE_DEFAULT);
    }

    /**
     * 获取保存的当前语言状态，无默认值
     *
     * @param context
     * @return
     */
    public static String getSaveLanguage(Context context) {
        return PreferenceUtils.getPreference(context, LANG_NAME, null);
    }

    /**
     * 设置本地默认语言环境
     *
     * @param context
     */
    private static void saveLanguage(Context context, String newLanguage) {
        Locale locale = SupportLanguageUtil.getSupportLanguage(newLanguage);
        String language = locale.getLanguage();
        if (locale.equals(Locale.CHINESE) || locale.equals(Locale.SIMPLIFIED_CHINESE) || locale.equals(Locale.TRADITIONAL_CHINESE)
                || (language != null && language.contains("zh")) || (language != null && language.contains("ZH")) ||
                (language != null && language.contains("CN")) || (language != null && language.contains("cn"))) {
            PreferenceUtils.putPreference(context, LanguageUtils.LANG_NAME, LanguageUtils.LANG_VALUE_SIMPLE_CHINESE);
        } else {
            PreferenceUtils.putPreference(context, LanguageUtils.LANG_NAME, LanguageUtils.LANG_VALUE_ENGLISH);
        }
    }

    /**
     * switch language
     * @param context
     */
    public static void switchLanguage(Context context){
        String mLanguage = getCurrentLanguage(context);
        if (mLanguage.equalsIgnoreCase(LanguageUtils.LANG_VALUE_SIMPLE_CHINESE)) {
            applyLanguage(context,LANG_VALUE_ENGLISH);
        } else {
            applyLanguage(context,LANG_VALUE_SIMPLE_CHINESE);
        }
    }

    /**
     * 应用语言
     *
     * @param context
     * @param newLanguage
     */
    public static Context applyLanguage(Context context, String newLanguage) {
        saveLanguage(context,newLanguage);

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Locale locale = SupportLanguageUtil.getSupportLanguage(newLanguage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // apply locale
//            configuration.setLocale(locale);
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
            Locale.setDefault(locale);
            return context.createConfigurationContext(configuration);
        } else {
            // updateConfiguration
            configuration.locale = locale;
        }
        resources.updateConfiguration(configuration, displayMetrics);
        return context;
    }

    /**
     * 获取语言配置context
     *
     * @param context
     * @param language
     * @return
     */
    public static Context attachBaseContext(Context context, String language) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return createConfigurationResources(context, language);
        } else {
            applyLanguage(context, language);
            return context;
        }
    }

    /**
     * 创建语言配置资源
     *
     * @param context
     * @param language
     * @return
     */
    @TargetApi(Build.VERSION_CODES.N)
    private static Context createConfigurationResources(Context context, String language) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale;
        if (TextUtils.isEmpty(language)) {//如果没有指定语言使用系统首选语言
            locale = SupportLanguageUtil.getSystemPreferredLanguage();
        } else {//指定了语言使用指定语言，没有则使用首选语言
            locale = SupportLanguageUtil.getSupportLanguage(language);
        }
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }
}
