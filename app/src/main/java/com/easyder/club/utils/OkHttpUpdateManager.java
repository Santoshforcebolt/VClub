//package com.easyder.club.utils;
//
//import androidx.annotation.NonNull;
//
//import com.lzy.okgo.OkGo;
//import com.lzy.okgo.model.Progress;
//import com.lzy.okgo.model.Response;
//import com.lzy.okgo.request.base.Request;
//import com.vector.update_app.HttpManager;
//import java.io.File;
//import java.util.Map;
//import okhttp3.OkHttpClient;
//
//
///**
// * 使用OkGo实现接口
// */
//
//public class OkHttpUpdateManager implements HttpManager {
//    /**
//     * 异步get
//     *
//     * @param url      get请求地址
//     * @param params   get参数
//     * @param callBack 回调
//     */
//    @Override
//    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
//        OkGo.<String>get(url).client(new OkHttpClient.Builder().build()).params(params).execute(new com.lzy.okgo.callback.StringCallback() {
//            @Override
//            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
//                callBack.onResponse(response.body());
//            }
//
//            @Override
//            public void onError(com.lzy.okgo.model.Response<String> response) {
//                super.onError(response);
//                callBack.onError("异常");
//            }
//        });
//    }
//
//    /**
//     * 异步post
//     *
//     * @param url      post请求地址
//     * @param params   post请求参数
//     * @param callBack 回调
//     */
//    @Override
//    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
//        OkGo.<String>post(url).client(new OkHttpClient.Builder().build()).params(params).execute(new com.lzy.okgo.callback.StringCallback() {
//            @Override
//            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
//                callBack.onResponse(response.body());
//            }
//
//            @Override
//            public void onError(com.lzy.okgo.model.Response<String> response) {
//                super.onError(response);
//                callBack.onError("异常");
//            }
//        });
//    }
//
//    /**
//     * 下载
//     *
//     * @param url      下载地址
//     * @param path     文件保存路径
//     * @param fileName 文件名称
//     * @param callback 回调
//     */
//    @Override
//    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull final FileCallback callback) {
//        OkGo.<File>get(url).client(new OkHttpClient.Builder().build()).
//                execute(new com.lzy.okgo.callback.FileCallback(path, fileName) {
//                    @Override
//                    public void onSuccess(Response<File> response) {
//                        callback.onResponse(response.body());
//                    }
//
//                    @Override
//                    public void onStart(Request<File, ? extends Request> request) {
//                        super.onStart(request);
//                        callback.onBefore();
//                    }
//
//                    @Override
//                    public void onError(Response<File> response) {
//                        super.onError(response);
//                        callback.onError("error");
//                    }
//
//                    @Override
//                    public void downloadProgress(Progress progress) {
//                        super.downloadProgress(progress);
//                        callback.onProgress(progress.fraction, progress.totalSize);
//                    }
//                });
//
//    }
//}