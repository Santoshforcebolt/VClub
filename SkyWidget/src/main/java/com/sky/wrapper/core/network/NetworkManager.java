package com.sky.wrapper.core.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.collection.ArrayMap;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.PutRequest;
import com.lzy.okgo.request.base.BodyRequest;
import com.lzy.okgo.request.base.Request;
import com.sky.wrapper.base.presenter.Callback;
import com.sky.wrapper.base.view.MvpView;
import com.sky.wrapper.utils.UIUtils;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author 刘琛慧
 *         date 2016/5/30.
 */
public class NetworkManager {

    private static NetworkManager instance;

    private NetworkManager() {
    }

    /**
     * 单例构造方法
     *
     * @return
     */
    public static NetworkManager getDefault() {
        if (instance == null) {
            synchronized (NetworkManager.class) {
                instance = new NetworkManager();
            }
            return instance;
        }
        return instance;
    }

    /**
     * Get请求
     * 执行加载数据,如果有做数据缓存，先从缓存里面读取数据,
     * 如果缓存数据有效，返回缓存数据，如果缓存失效，重新从
     * 网路请求数据，并将数据缓存到本地SD卡,记录缓存写入时间
     *
     * @param callback 数据加载成功后的回调方法
     */
    public void doGet(RequestInfo requestInfo, final Callback callback) {
        if (!isNetworkConnected()) {    //无网络请求
            ResponseInfo responseInfo = new ResponseInfo(ResponseInfo.NO_INTERNET_ERROR);
            responseInfo.setUrl(requestInfo.url);
            callback.onError(responseInfo);
            return;
        }

        GetRequest getRequest = OkGo.get(requestInfo.url);
        ArrayMap<String, Serializable> params = requestInfo.requestParams;
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Serializable> entry : params.entrySet()) {
                getRequest.params(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        setRequestHeader(requestInfo.getHeaderParams(), getRequest);
        getRequest.execute(new ResponseCallback(callback, requestInfo));
    }

    /**
     * Post请求
     * 执行加载数据,如果有做数据缓存，先从缓存里面读取数据,
     * 如果缓存数据有效，返回缓存数据，如果缓存失效，重新从
     * 网路请求数据，并将数据缓存到本地SD卡,记录缓存写入时间
     *
     * @param requestInfo 请求体
     * @param callback    数据加载成功后的回调方法
     */
    public void doPost(RequestInfo requestInfo, final Callback callback) {
        if (!isNetworkConnected()) {
            ResponseInfo responseInfo = new ResponseInfo(ResponseInfo.NO_INTERNET_ERROR);
            callback.onError(responseInfo);
            return;
        }
        PostRequest request = OkGo.post(requestInfo.getUrl());
        setRequestParams(requestInfo.getRequestParams(), request);
        setRequestHeader(requestInfo.getHeaderParams(), request);
        request.execute(new ResponseCallback(callback, requestInfo));
    }

    /**
     * Put请求
     * 执行加载数据,如果有做数据缓存，先从缓存里面读取数据,
     * 如果缓存数据有效，返回缓存数据，如果缓存失效，重新从
     * 网路请求数据，并将数据缓存到本地SD卡,记录缓存写入时间
     *
     * @param callback 数据加载成功后的回调方法
     */
    public void doPut(RequestInfo requestInfo, final Callback callback) {
        if (!isNetworkConnected()) {
            ResponseInfo responseInfo = new ResponseInfo(ResponseInfo.NO_INTERNET_ERROR);
            callback.onError(responseInfo);
            return;
        }
        PutRequest putRequest = OkGo.put(requestInfo.getUrl());
        ArrayMap<String, Serializable> params = requestInfo.getRequestParams();
        switch (requestInfo.getRequestType()) {
            case RequestInfo.REQUEST_PUT:
                setRequestParams(params, putRequest);
                break;
            case RequestInfo.REQUEST_PUT_JSON: //默认只取第一个参数的value值
                if (params != null && params.size() > 0) {
                    for (Map.Entry<String, Serializable> entry : params.entrySet()) {
                        putRequest.upJson(entry.getValue().toString());
                        break;
                    }
                }
                break;

        }
        setRequestHeader(requestInfo.getHeaderParams(), putRequest);
        putRequest.execute(new ResponseCallback(callback, requestInfo));
    }

    /**
     * 自定义请求
     * @param requestInfo
     * @param callback
     */
    public void doCustomer(RequestInfo requestInfo, final Callback callback) {
        if (!isNetworkConnected()) {    //无网络请求
            ResponseInfo responseInfo = new ResponseInfo(ResponseInfo.NO_INTERNET_ERROR);
            responseInfo.setUrl(requestInfo.url);
            callback.onError(responseInfo);
            return;
        }

        if (requestInfo.request != null) {
            requestInfo.request.execute(new ResponseCallback(callback, requestInfo));
        }
    }

    /**
     * 上传
     *
     * @param url
     * @param params
     * @param callback
     */
    public void upload(String url, ArrayMap<String, Serializable> params, FileCallback callback) {
        if (params != null) {
            setRequestParams(params, OkGo.post(url)).execute(callback);
        }
    }

    /**
     * 下载
     *
     * @param url
     * @param params
     * @param callback
     */
    public void download(String url, ArrayMap<String, Serializable> params, FileCallback callback) {
        if (params != null) {
            setRequestParams(params, OkGo.post(url)).execute(callback);
        }
    }

    /**
     * 设置请求头
     *
     * @param params
     * @param request
     */
    private Request setRequestHeader(ArrayMap<String, String> params, Request request) {
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                request.headers(entry.getKey(), entry.getValue());
            }
        }
        return request;
    }

    /**
     * 设置请求参数
     *
     * @param params  请求参数
     * @param request 请求类型
     * @return
     */
    private BodyRequest setRequestParams(ArrayMap<String, Serializable> params, BodyRequest request) {
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Serializable> entry : params.entrySet()) {
                if (entry.getValue() instanceof File) {
                    request.params(entry.getKey(), (File) entry.getValue());
                } else if (entry.getValue() instanceof List) {
                    request.addFileParams(entry.getKey(), (List<File>) entry.getValue());
                    request.isMultipart(true);
                } else {
                    request.params(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
        }
        return request;
    }

    /**
     * 检查网络是否已经连接
     *
     * @return
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) UIUtils.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        return mNetworkInfo != null && mNetworkInfo.isConnected();
    }

    /**
     * 取消指定Tag的请求
     *
     * @param view
     */
    public void cancellAll(MvpView view) {
        if (view != null) {
            OkGo.getInstance().cancelTag(view);
        }
    }

    /**
     * 销毁对象
     */
    public void destory() {
        instance = null;
    }


}
