package com.sky.wrapper.core.network;

import androidx.collection.ArrayMap;

import com.lzy.okgo.request.base.Request;
import com.sky.wrapper.ManagerConfig;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.core.model.Entity;

import java.io.Serializable;


/**
 * 请求体
 */
public class RequestInfo {
    public String host = ManagerConfig.getInstance().getBaseHost();

    public static final int REQUEST_GET = 0x10; //查询数据请求
    public static final int REQUEST_POST = 0x20; //新增和修改数据请求
    public static final int REQUEST_PUT = 0x30; //put请求
    public static final int REQUEST_PUT_JSON = 0x31; //put请求 直接上传json
    public static final int REQUEST_CUSTOMER = 0x41; //自定义请求
    public static final int REQUEST_DELETE = 0x50; //删除请求
    public static final int PAGE_SIZE = 10; //默认数据分页数据条数


    private int requestType; //请求的类型
    private int actionTye; //请求接口要做的操作类型。
    public long dataExpireTime; //数据缓存时间默认为0即不缓存数据

    public ArrayMap<String, Serializable> requestParams; //请求参数
    private ArrayMap<String, String> headerParams; //请求头参数

    public boolean needShowDialog = true; //是否需要显示加载对话框
    public boolean needMockData = false; //是否需要模拟数据，此标识仅用于接口已定义，但未实现的前提下才能设置为true
    public String url;
    public String api; //请求的api
    public Class<? extends BaseVo> dataClass; //请求结果Vo的class
    private Class<? extends Entity> entityClass; //请求结果entity的class

    public Request request;

    public RequestInfo(String api, Class<? extends BaseVo> dataClass) {
        this.api = api;
        this.dataClass = dataClass;
        this.url = String.format("%1$s%2$s", host, api);
    }

    public RequestInfo(String url, long dataExpireTime, Class<? extends BaseVo> dataClass) {
        this(url, dataClass);
        this.dataExpireTime = dataExpireTime;
    }

    public RequestInfo(boolean isDirect, String api, Class<? extends BaseVo> dataClass) {
        this.api = api;
        this.dataClass = dataClass;
        this.url = isDirect ? api : String.format("%1$s%2$s", host, api);
    }

    public RequestInfo(boolean isDirect, String api, Class<? extends BaseVo> dataClass, ArrayMap<String, String> headerParams) {
        this.api = api;
        this.dataClass = dataClass;
        this.url = isDirect ? api : String.format("%1$s%2$s", host, api);
        this.headerParams = headerParams;
    }


    /**
     * @param isDirect
     * @param requestType  此处用于扩展  此处需要主动在请求体内做处理
     * @param api
     * @param dataClass
     * @param headerParams
     */
    public RequestInfo(boolean isDirect, int requestType, String api, Class<? extends BaseVo> dataClass, ArrayMap<String, String> headerParams) {
        this.api = api;
        this.requestType = requestType;
        this.dataClass = dataClass;
        this.url = isDirect ? api : String.format("%1$s%2$s", host, api);
        this.headerParams = headerParams;
    }

    /**
     * 用于自定义请求
     *
     * @param api
     * @param request
     */
    public RequestInfo(boolean isDirect, String api, Request request, Class<? extends BaseVo> dataClass) {
        this.api = api;
        this.url = isDirect ? api : String.format("%1$s%2$s", host, api);
        this.requestType = REQUEST_CUSTOMER;
        this.request = request;
        this.dataClass = dataClass;
    }



    public Class<? extends BaseVo> getDataClass() {
        return dataClass;
    }

    public void setDataClass(Class<? extends BaseVo> dataClass) {
        this.dataClass = dataClass;
    }

    public Class<? extends Entity> getEntityClass() {
        return entityClass;
    }

    public int getActionTye() {
        return actionTye;
    }

    public void setActionTye(int actionTye) {
        this.actionTye = actionTye;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public long getDataExpireTime() {
        return dataExpireTime;
    }

    public void setDataExpireTime(long dataExpireTime) {
        this.dataExpireTime = dataExpireTime;
    }

    public ArrayMap<String, Serializable> getRequestParams() {
        return requestParams;
    }

    public ArrayMap<String, Serializable> put(String key, Serializable value) {
        requestParams.put(key, value);
        return requestParams;
    }

    public void setRequestParams(ArrayMap<String, Serializable> requestParams) {
        this.requestParams = requestParams;
    }

    public ArrayMap<String, String> getHeaderParams() {
        return headerParams;
    }

    public void setHeaderParams(ArrayMap<String, String> headerParams) {
        this.headerParams = headerParams;
    }

    public boolean isNeedShowDialog() {
        return needShowDialog;
    }

    public void setNeedShowDialog(boolean needShowDialog) {
        this.needShowDialog = needShowDialog;
    }

    public boolean isNeedMockData() {
        return needMockData;
    }

    public void setNeedMockData(boolean needMockData) {
        this.needMockData = needMockData;
    }
}
