package com.easyder.club.module.basic.presenter;


import androidx.collection.ArrayMap;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PutRequest;
import com.sky.wrapper.core.manager.DataManager;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.core.network.RequestInfo;
import com.sky.wrapper.core.network.ResponseInfo;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author:  winds
 * Data:    2018/8/20
 * Version: 1.0
 * Desc:
 */


public class CustomerPresenter extends CommonPresenter {

    public List<String> list = new ArrayList<>();

    @Override
    public void onError(ResponseInfo responseInfo) {
        if (isViewAttached()) {
            if (responseInfo.getUrl() != null) {
                if (list != null && list.size() > 0) {
                    for (String url : list) {
                        if (responseInfo.getUrl().contains(url)) {
                            requestCount--;
                            getView().onStopLoading();
                            getView().onError(responseInfo);
                            return;
                        }
                    }
                }
            }
        }
        super.onError(responseInfo);
    }

    /**
     * 自定义get请求
     * @param url
     * @param params
     * @param dataClass
     */
    public void doGetCustomer(String url, ArrayMap<String, Serializable> params, Class<? extends BaseVo> dataClass) {
        if (isViewAttached() && needDialog && requestCount >= 0) {
            getView().onLoading();
        }
        GetRequest request = OkGo.get(url);

        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Serializable> entry : params.entrySet()) {
                request.params(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }

        RequestInfo requestInfo = new RequestInfo(true, url, request, dataClass);
        DataManager.getDefault().loadData(requestInfo, this);
        requestCount++;
    }


    /**
     * 自定义put请求
     * @param url
     * @param params
     * @param dataClass
     */
    public void doPutCustomer(String url, ArrayMap<String, Serializable> params, Class<? extends BaseVo> dataClass) {
        if (isViewAttached() && needDialog && requestCount >= 0) {
            getView().onLoading();
        }
        PutRequest request = OkGo.put(url);
//        request.client(new OkHttpUrlLoader.Factory().getHttpClient());

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

        RequestInfo requestInfo = new RequestInfo(false, url, request, dataClass);
        DataManager.getDefault().loadData(requestInfo, this);
        requestCount++;
    }

    /**
     * 自定义put请求
     * @param url
     * @param params
     * @param dataClass
     */
    public void doPutJsonCustomer(String url, ArrayMap<String, Serializable> params, Class<? extends BaseVo> dataClass) {
        if (isViewAttached() && needDialog && requestCount >= 0) {
            getView().onLoading();
        }
        PutRequest request = OkGo.put(url);
//        request.client(new OkHttpUrlLoader.Factory().getHttpClient());

        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Serializable> entry : params.entrySet()) {
                request.upJson(entry.getValue().toString());
                break;
            }
        }

        RequestInfo requestInfo = new RequestInfo(true, url, request, dataClass);
        DataManager.getDefault().loadData(requestInfo, this);
        requestCount++;
    }

    /**
     * 添加要过滤的url  添加后需要手动处理对应的onError方法
     * @param url
     */
    public void addFiltersUrl(String url) {
        if (!list.contains(url)) {
            list.add(url);
        }
    }

    public void clearFiltersUrl() {
        list.clear();
    }


    public void removeFiltersUrl(String url) {
        list.remove(url);
    }
}
