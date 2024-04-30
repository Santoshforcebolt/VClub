package com.sky.wrapper.base.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.collection.ArrayMap;
import androidx.fragment.app.Fragment;

import com.sky.widget.usage.ToastView;
import com.sky.wrapper.ManagerConfig;
import com.sky.wrapper.base.view.MvpView;
import com.sky.wrapper.core.manager.DataManager;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.core.network.RequestInfo;
import com.sky.wrapper.core.network.ResponseInfo;
import com.sky.wrapper.utils.LogUtils;

import java.io.Serializable;


/**
 * @author 刘琛慧
 * date 2015/10/27.
 */
public class MvpBasePresenter extends MvpPresenter<MvpView> implements Callback {

    protected boolean needDialog = true;
    public int requestCount;

    /**
     * 请求成功，回调View层方法处理成功的结果
     *
     * @param responseInfo 包含的返回数据的BaseVo子类对象
     */
    @Override
    public void onSuccess(ResponseInfo responseInfo) {
        if (isViewAttached()) {
            requestCount--;
            getView().beforeSuccess();
            getView().showContentView(responseInfo.url, responseInfo.dataVo);
            if (requestCount == 0) {
                getView().onStopLoading();
            }
        } else {
            LogUtils.e("View已被销毁，onSuccess方法无法回调showContentView方法 ==> " + viewClassName);
        }
    }

    /**
     * 请求失败，回调View层的方法处理错误信息
     *
     * @param responseInfo 包含错误码和错误信息的BaseVo子类对象
     */
    @Override
    public void onError(ResponseInfo responseInfo) {
        if (isViewAttached()) {
            requestCount--;
            getView().onStopLoading();
            switch (responseInfo.getState()) {
                case ResponseInfo.FAILURE:  //请求出错
                case ResponseInfo.CACHE_PARSE_ERROR://缓存数据解析错误
                case ResponseInfo.JSON_PARSE_ERROR://Json数据解析错误
                case ResponseInfo.RESPONSE_FAILURE:
                    if (responseInfo != null && !TextUtils.isEmpty(responseInfo.getUrl())) {
                        //屏蔽登陆接口的错误消息，交有具体界面处理
                        if (responseInfo.getUrl().contains("api/home/v1/auth/login")||
                                responseInfo.getUrl().contains("api/home/v1/auth/code-login")){
                            break;
                        }
                    }
                    showToast(responseInfo.msg);
                    break;
                case ResponseInfo.TIME_OUT: //请求超时
                    showToast("网络连接不稳定，请检查网络设置");
                    break;
                case ResponseInfo.NO_INTERNET_ERROR:    //无网络连接
                    showToast("网络不可用，请检查网络设置");
                    break;
                case ResponseInfo.SERVER_UNAVAILABLE:   //服务器无法访问
                    showToast("接口访问失败");
                    break;
                case ResponseInfo.UN_LOGIN: //未登录或登录失效
                    login();
                    break;
                case ResponseInfo.UNSET_TYPE:   //未设置客户端
                    setClientType();
                    break;
                default:
                    break;
            }
            getView().onError(responseInfo);
        } else {
            LogUtils.e("MvpView已销毁，onError方法无法回调MvpView层的方法 ==> " + viewClassName);
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        //取消默认的还未完成的请求
        DataManager.getDefault().onViewDetach(getView());
    }

    /**
     * Get请求
     *
     * @param url
     * @param params 参数
     */
    public void getData(String url, ArrayMap<String, Serializable> params) {
        getData(url, params, BaseVo.class);
    }

    /**
     * Get请求
     *
     * @param url
     * @param dataClass
     */
    public void getData(String url, Class<? extends BaseVo> dataClass) {
        getData(url, null, dataClass);
    }

    /**
     * Get请求
     *
     * @param url
     * @param params    参数
     * @param dataClass 对象类型
     */
    public void getData(String url, ArrayMap<String, Serializable> params, Class<? extends BaseVo> dataClass) {
        getData(false, url, null, params, dataClass);
    }

    /**
     * Get请求
     *
     * @param url
     * @param params    参数
     * @param dataClass 对象类型
     */
    public void getData(boolean isDirect, String url, ArrayMap<String, String> headerParams, ArrayMap<String, Serializable> params, Class<? extends BaseVo> dataClass) {
        if (isViewAttached() && needDialog && requestCount == 0) {
            getView().onLoading();
        }

        RequestInfo requestInfo = new RequestInfo(isDirect, url, dataClass);
        requestInfo.setRequestType(RequestInfo.REQUEST_GET);
        requestInfo.setRequestParams(params);
        requestInfo.setHeaderParams(headerParams);
        DataManager.getDefault().loadData(requestInfo, this);
        requestCount++;
    }

    /**
     * Post请求
     *
     * @param url
     * @param params 参数
     */
    public void postData(String url, ArrayMap<String, Serializable> params) {
        postData(url, params, null);
    }

    /**
     * Post请求
     *
     * @param url
     * @param dataClass 对象类型
     */
    public void postData(String url, Class<? extends BaseVo> dataClass) {
        postData(url, null, dataClass);
    }

    /**
     * Post请求
     *
     * @param url
     * @param params    参数
     * @param dataClass 对象类型
     */
    public void postData(String url, ArrayMap<String, Serializable> params, Class<? extends BaseVo> dataClass) {
        postData(false, url, null, params, dataClass);
    }

    /**
     * Post请求
     *
     * @param url
     * @param params    参数
     * @param dataClass 对象类型
     */
    public void postData(boolean isDirect, String url, ArrayMap<String, String> headerParams, ArrayMap<String, Serializable> params, Class<? extends BaseVo> dataClass) {
        if (isViewAttached() && needDialog && requestCount >= 0) {
            getView().onLoading();
        }
        RequestInfo requestInfo = new RequestInfo(isDirect, url, dataClass);
        requestInfo.setRequestType(RequestInfo.REQUEST_POST);
        requestInfo.setRequestParams(params);
        requestInfo.setHeaderParams(headerParams);
        DataManager.getDefault().loadData(requestInfo, this);
        requestCount++;
    }

    /**
     * put请求
     *
     * @param url
     * @param params    参数
     * @param dataClass 对象类型
     */
    public void putData(String url, ArrayMap<String, Serializable> params, Class<? extends BaseVo> dataClass) {
        putData(false, url, null, params, dataClass);
    }

    /**
     * put请求
     *
     * @param url
     * @param params    参数
     * @param dataClass 对象类型
     */
    public void putData(boolean isDirect, String url, ArrayMap<String, String> headerParams, ArrayMap<String, Serializable> params, Class<? extends BaseVo> dataClass) {
        if (isViewAttached() && needDialog && requestCount >= 0) {
            getView().onLoading();
        }
        RequestInfo requestInfo = new RequestInfo(isDirect, url, dataClass);
        requestInfo.setRequestType(RequestInfo.REQUEST_PUT);
        requestInfo.setRequestParams(params);
        requestInfo.setHeaderParams(headerParams);
        DataManager.getDefault().loadData(requestInfo, this);
        requestCount++;
    }

    /**
     * 此处需要注意requestType 必须是定义好的种类
     *
     * @param requestInfo 参数可参考如上请求的参数
     */
    public void loadData(RequestInfo requestInfo) {
        if (isViewAttached() && needDialog && requestCount == 0) {
            getView().onLoading();
        }
        if (requestInfo.getRequestType() == 0) { //如果未指定请求类型 默认请求类未get
            requestInfo.setRequestType(RequestInfo.REQUEST_GET);
        }
        DataManager.getDefault().loadData(requestInfo, this);
        requestCount++;
    }

    /**
     * 判断请求时是否需要Dialog
     *
     * @return 默认true
     */
    public boolean isNeedDialog() {
        return needDialog;
    }

    /**
     * 设置请求时是否需要加载进度条   需要在请求前设置
     *
     * @param needDialog 默认true
     */
    public void setNeedDialog(boolean needDialog) {
        this.needDialog = needDialog;
    }

    /**
     * 设置客户端类型
     */
    protected void setClientType() {

    }

    public int getRequestCount() {
        return requestCount;
    }

    /**
     * 登录
     */
    protected void login() {

    }

    protected Context getContext() {
        return getView() instanceof Context ? (Context) getView() : ((Fragment) getView()).getContext();
    }

    /**
     * 提示
     *
     * @param msg
     */
    protected void showToast(String msg) {
        ToastView.showToastInCenter(ManagerConfig.getInstance().getApplicationContext(), msg, Toast.LENGTH_SHORT);
    }
}
