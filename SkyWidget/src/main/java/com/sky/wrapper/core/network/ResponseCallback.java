package com.sky.wrapper.core.network;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.sky.wrapper.base.presenter.Callback;
import com.sky.wrapper.core.manager.CacheManager;
import com.sky.wrapper.core.manager.DataManager;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.LogUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Call;

/**
 * Auther:  winds
 * Data:    2017/7/1
 * Desc:    响应回调
 */

public class ResponseCallback extends StringCallback {
    public Callback callback;
    public String filePath;
    public RequestInfo requestInfo;

    public ResponseCallback(Callback callback, RequestInfo requestInfo) {
        this.callback = callback;
        this.requestInfo = requestInfo;
    }

    public ResponseCallback(Callback callback, RequestInfo requestInfo, String filePath) {
        this.callback = callback;
        this.requestInfo = requestInfo;
        this.filePath = filePath;
    }

    @Override
    public void onSuccess(Response<String> response) {
        onSuccess(response.body(), response.getRawCall(), response.getRawResponse());
    }

    @Override
    public void onError(Response<String> response) {
        super.onError(response);

        ResponseInfo responseInfo;
        Throwable error = response.getException();
        if (error instanceof SocketTimeoutException) {
            responseInfo = new ResponseInfo(ResponseInfo.TIME_OUT);
        } else {
            responseInfo = new ResponseInfo(ResponseInfo.FAILURE);
            responseInfo.setMsg("服务器连接失败");
            responseInfo.setErrorObject(error);
            if (response != null && response.getRawResponse() != null) {
                responseInfo.setUrl(response.getRawResponse().request().url().url().getPath());
            }
        }

        DataManager.getDefault().postCallback(callback, responseInfo);
    }

    public void onSuccess(String content, Call call, okhttp3.Response response) {
        ResponseInfo responseInfo;
        if (response.isSuccessful()) {
            String type = response.body().contentType().subtype();
            if (type.equals("json")) {
                dispatchJsonResult(content, response.request().url().url().getPath(), type);
            } else {
                responseInfo = new ResponseInfo(ResponseInfo.FAILURE);
                responseInfo.setResponseType(type);
                responseInfo.setUrl(response.request().url().url().getPath());
                responseInfo.setMsg("无法解析请求结果");
                try {
                    responseInfo.setRawData(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DataManager.getDefault().postCallback(callback, responseInfo);
            }
        } else {
            responseInfo = new ResponseInfo(ResponseInfo.FAILURE);
            responseInfo.setState(ResponseInfo.SERVER_UNAVAILABLE);
            DataManager.getDefault().postCallback(callback, responseInfo);
        }
    }

    private void dispatchJsonResult(String response, String url, String type) {
        ResponseInfo responseInfo;
        try {
            JSONObject jsonObject = JSON.parseObject(response);
            responseInfo = new ResponseInfo(parseResultStatus(jsonObject.getString("state")), jsonObject.getString("msg"));
            responseInfo.setResponseType(type);
            url = url.substring(1, url.length());
            responseInfo.setUrl(url);

            String data = jsonObject.getString("rows");
            if (TextUtils.isEmpty(data)) {
                data = jsonObject.getString("data");
                LogUtils.d("url   " + url + "  state  " + jsonObject.getString("state") + "   data: " + data);
            } else {
                LogUtils.d("url   " + url + "  state  " + jsonObject.getString("state") + "   rows: " + data);
            }

            if (responseInfo.getState() != ResponseInfo.SUCCESS) {
                DataManager.getDefault().postCallback(callback, responseInfo);
                return;
            }

            BaseVo baseVo = BaseVo.parseDataVo(data, requestInfo.getDataClass());
            if (responseInfo.getState() != ResponseInfo.SUCCESS && baseVo == null) {
                responseInfo.setState(ResponseInfo.JSON_PARSE_ERROR);
                responseInfo.setMsg("请求结果数据解析失败！");
            }

            responseInfo.setDataVo(baseVo);
            DataManager.getDefault().postCallback(callback, responseInfo);
            //缓存数据
            if (requestInfo.getDataExpireTime() > 0 && !TextUtils.isEmpty(data)) {
                String key = CacheManager.getDefault().sortUrl(requestInfo.getUrl(), requestInfo.getRequestParams());
                CacheManager.getDefault().writeToCache(key, data);
            }

        } catch (JSONException ex) {
            responseInfo = new ResponseInfo(ResponseInfo.JSON_PARSE_ERROR);
            LogUtils.e(ex);
            responseInfo.setMsg(ex.getMessage());
            DataManager.getDefault().postCallback(callback, responseInfo);
        } catch (Exception e) {
            LogUtils.e(e);
            LogUtils.e("数据处理异常，原始数据：" + response);
            responseInfo = new ResponseInfo(ResponseInfo.LOGIC_ERROR);
            responseInfo.setResponseType(type);
            DataManager.getDefault().postCallback(callback, responseInfo);
        }
    }

    public int parseResultStatus(String status) {
        if ("success".equals(status)) {
            return ResponseInfo.SUCCESS;
        } else if ("failure".equals(status)) {
            return ResponseInfo.RESPONSE_FAILURE;
        } else if ("relogin".equals(status)) {
            return ResponseInfo.UN_LOGIN;
        }
        return ResponseInfo.FAILURE;
    }

}
