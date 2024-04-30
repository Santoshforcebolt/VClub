package com.easyder.club;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.easyder.club.module.common.vo.CustomerBean;
import com.sky.wrapper.ManagerConfig;
import com.sky.wrapper.utils.LanguageUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Auther:  winds
 * Data:    2018/2/27
 * Desc:
 */
public class App extends MultiDexApplication {

    private static Application instance;
    private static RefWatcher watcher;
    private static CustomerBean customerBean;
    private static String cid;

    @Override
    protected void attachBaseContext(Context context) {
        String name = LanguageUtils.getSaveLanguage(context);
        if (TextUtils.isEmpty(name)) {
            super.attachBaseContext(context);
        } else {
            super.attachBaseContext(LanguageUtils.applyLanguage(context, name));
        }
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        watcher = LeakCanary.install(this);

        instance = this;
        //初始wrapper包的manager
        ManagerConfig.getInstance().init(this)
                .setBaseHost(ApiConfig.HOST)
                .initHttpClient()
                .setLogConfig(AppConfig.DEFAULT_LOG_PATH, BuildConfig.DEBUG, false);

//        Bugly.init(this, AppConfig.BUGLY_ID, BuildConfig.DEBUG);
    }

    public static Application getInstance() {
        return instance;
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    public static boolean isLogin() {
        return getCustomer() != null && !TextUtils.isEmpty(getCid());
    }

    /**
     * get cid
     *
     * @return
     */
    public static String getCid() {
        if (TextUtils.isEmpty(cid)) {
            cid = Helper.getCid();
        }
        return cid;
    }

    /**
     * set cid
     *
     * @param cid
     */
    public static void setCid(String cid) {
        App.cid = cid;
        Helper.saveCid(cid);
    }

    /**
     * 获取会员信息
     *
     * @return
     */
    public static CustomerBean getCustomer() {
        if (customerBean == null) {
            customerBean = Helper.getCustomerInfo();
        }
        return customerBean;
    }

    /**
     * 保存会员信息
     *
     * @param customerBean
     */
    public static void setCustomerBean(CustomerBean customerBean) {
        App.customerBean = customerBean;
        Helper.saveCustomerInfo(customerBean);
    }
}
