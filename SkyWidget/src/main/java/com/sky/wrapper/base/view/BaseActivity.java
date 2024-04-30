package com.sky.wrapper.base.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.annotation.RequiresApi;
import androidx.appcompat.view.ContextThemeWrapper;

import com.sky.widget.R;
import com.sky.wrapper.utils.LanguageUtils;

import java.util.Locale;

import static com.sky.wrapper.utils.SupportLanguageUtil.getSupportLanguage;

/**
 * Author: sky on 2022/8/10 20:19.
 * Email:xcode126@126.com
 * Desc: 适配多语言
 */
public class BaseActivity extends Activity {

    @Override
    protected void attachBaseContext(Context newBase) {
//        Integer language = SpUtil.getInt(newBase, Cons.SP_KEY_OF_CHOOSED_LANGUAGE, -1);
        String language = LanguageUtils.getCurrentLanguage(newBase);
        Context context = attachBaseContext(newBase, language);
        final Configuration configuration = context.getResources().getConfiguration();
        // 此处的ContextThemeWrapper是androidx.appcompat.view包下的
        // 你也可以使用android.view.ContextThemeWrapper，但是使用该对象最低只兼容到API 17
        // 所以使用 androidx.appcompat.view.ContextThemeWrapper省心
        final ContextThemeWrapper wrappedContext = new ContextThemeWrapper(context,
                R.style.Theme_AppCompat_Empty) {
            @Override
            public void applyOverrideConfiguration(Configuration overrideConfiguration) {
                if (overrideConfiguration != null) {
                    overrideConfiguration.setTo(configuration);
                }
                super.applyOverrideConfiguration(overrideConfiguration);
            }
        };
        super.attachBaseContext(wrappedContext);
    }

    // 下面是多语言切换方法
    public static Context attachBaseContext(Context context, String language) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return createConfigurationResources(context, language);
        } else {
            applyLanguage(context, language);
            return context;
        }
    }

    /**
     *
     * @param context
     * @param language
     * @return
     */
    @TargetApi(Build.VERSION_CODES.N)
    private static Context createConfigurationResources(Context context, String language) {
        Resources resources = context.getResources();
        final Configuration configuration = resources.getConfiguration();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        Locale locale;
        if (TextUtils.isEmpty(language)) {
            // 如果没有指定语言使用系统首选语言
            locale = getSystemPreferredLanguage();
        } else {
            // 指定了语言使用指定语言，没有则使用首选语言
            locale = getSupportLanguage(language);
        }
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, dm);
        return context;
    }

    /**
     *
     * @param context
     * @param newLanguage
     */
    public static void applyLanguage(Context context, String newLanguage) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = getSupportLanguage(newLanguage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            DisplayMetrics dm = resources.getDisplayMetrics();
            // apply locale
            configuration.setLocale(locale);
            resources.updateConfiguration(configuration, dm);
        } else {
            // updateConfiguration
            DisplayMetrics dm = resources.getDisplayMetrics();
            configuration.locale = locale;
            resources.updateConfiguration(configuration, dm);
        }
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
