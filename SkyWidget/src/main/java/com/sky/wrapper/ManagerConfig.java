package com.sky.wrapper;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.CookieStore;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.utils.HttpUtils;
import com.sky.wrapper.core.scheduler.SafeDispatchHandler;
import com.sky.wrapper.utils.LogUtils;
import com.sky.wrapper.utils.WrapperDBCookieStore;
import com.squareup.leakcanary.RefWatcher;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * Auther:  winds
 * Data:    2018/3/12
 * Version: 1.0
 * Desc:
 */

public class ManagerConfig {
    private Application context;
    private String host;
    private Handler mMainHandler;
    public RefWatcher watcher;

    private ManagerConfig() {
        mMainHandler = new SafeDispatchHandler(Looper.getMainLooper());
    }


    private static class ConfigBuilder {
        private static ManagerConfig holder = new ManagerConfig();
    }

    /**
     * 获取实例化的方法  第一次实例化 请在application   同时调用init方法
     *
     * @return
     */
    public static ManagerConfig getInstance() {
        return ConfigBuilder.holder;
    }

    /**
     * 第一次实例化调用
     *
     * @param app
     * @return
     */
    public ManagerConfig init(Application app) {
        context = app;
        return this;
    }

    /**
     * 设置通用的host 如不设置  默认为空
     *
     * @param host
     * @return
     */
    public ManagerConfig setBaseHost(String host) {
        this.host = host;
        return this;
    }

    /**
     * 获取Application对象
     *
     * @return
     */
    public Application getApplicationContext() {
        HttpUtils.checkNotNull(context, "please call ManagerConfig.getInstance().init() first in application!");
        return context;
    }

    /**
     * 获取主线程handler
     *
     * @return
     */
    public Handler getMainThreadHandler() {
        HttpUtils.checkNotNull(context, "please call ManagerConfig.getInstance().init() first in application!");
        return mMainHandler;
    }

    /**
     * 获取主线程threadId
     *
     * @return
     */
    public long getMainThreadId() {
        return getMainThreadHandler().getLooper().getThread().getId();
    }

    /**
     * 获取通用host 在未设置时返回空
     *
     * @return
     */
    public String getBaseHost() {
        return host == null ? "" : host;
    }

    /**
     * 初始化okgo 默认提供通用实现
     *
     * @return
     */
    public ManagerConfig initHttpClient() {
        return initHttpClient(new DBCookieStore(context),null);
    }

    /**
     * 自定义cookieStore
     *
     * @param cookieStore
     * @return
     */
    public ManagerConfig initHttpClient(CookieStore cookieStore, HttpHeaders headers) {
        HttpUtils.checkNotNull(context, "please call ManagerConfig.getInstance().init() first in application!");
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo ");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        //全局的读取超时时间
        builder.readTimeout(20 * 1000, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(20 * 1000, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(20 * 1000, TimeUnit.MILLISECONDS);

        //使用数据库保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new WrapperDBCookieStore(context)));
//        builder.cookieJar(new CookieJarImpl(cookieStore));
        return initHttpClient(builder.build(), 1,headers);
    }

    /**
     * 初始化OkGo  根据需求定制httpclient
     *
     * @param client     httpclient
     * @param retryCount 重试次数
     * @return
     */
    public ManagerConfig initHttpClient(OkHttpClient client, int retryCount,HttpHeaders headers) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.put("version", BuildConfig.VERSION_CODE + "");//版本号
//        headers.put("lang", LanguageUtils.getCurrentLanguage(context));//zh表示中文 en表示英文
//        headers.put("os", "1");// 1表示android 2表示ios 3表示其他
//        headers.put("make", "1");//手机品牌
//        headers.put("model", "1");//手机型号
//        headers.put("deviceId", SystemUtils.getDeviceId(context));//设备惟一标识
//        headers.put("Content-Type", "application/json;charset=UTF-8");

        HttpUtils.checkNotNull(context, "please call ManagerConfig.getInstance().init() first in application!");
        OkGo.getInstance().init(context)
                .setRetryCount(1)
                .addCommonHeaders(headers)
                .setOkHttpClient(client);
        return this;
    }

    public ManagerConfig setLogConfig(String path, boolean debugable, boolean writeable) {
        LogUtils.setLogConfig(path, debugable, writeable);
        return this;
    }

    public ManagerConfig providRefWatcher(RefWatcher watcher) {
        this.watcher = watcher;
        return this;
    }
}
