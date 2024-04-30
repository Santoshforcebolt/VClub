package com.easyder.club.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.core.content.ContextCompat;

import com.easyder.club.R;
import com.easyder.club.module.basic.event.AccountChanged;
import com.easyder.club.module.basic.event.AccountIml;
import com.easyder.club.module.common.AlertActivity;
import com.sky.widget.autolayout.utils.AutoUtils;
import com.sky.wrapper.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


/**
 * Author：sky on 2020/6/13 15:54.
 * Email：xcode126@126.com
 * Desc：WebView Utils
 */

public class WebViewUtils {

    /**
     * font head
     */
    public static final String FORMAT_FONT_HEAD = "<!DOCTYPE html PUBLIC \"-//WAPFORUM//DTD XHTML Mobile 1.0//EN\" \"http://www.wapforum.org/DTD/xhtml-mobile10.dtd\">\n" +
            "\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
    /**
     * font css
     */
    public static final String FORMAT_FONT_CSS = "<!DOCTYPE html PUBLIC \"-//WAPFORUM//DTD XHTML Mobile 1.0//EN\" \"http://www.wapforum.org/DTD/xhtml-mobile10.dtd\">\n" +
            "\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">" +
            "<style type=\"text/css\"> img {" +
            "width:100%;" +//限定图片宽度填充屏幕
            "height:auto;" +//限定图片高度自动
            "}" +
            "body {" +
            "margin-right:15px;" +//限定网页中的文字右边距为15px(可根据实际需要进行行管屏幕适配操作)
            "margin-left:15px;" +//限定网页中的文字左边距为15px(可根据实际需要进行行管屏幕适配操作)AutoUtils.getPercentWidthSize(16)
            "word-wrap:break-word;" +//允许自动换行(汉字网页应该不需要这一属性,这个用来强制英文单词换行,类似于word/wps中的西文换行)
            "}" + "*{color:#333333;font-size:" + 14 + "px}" +
            "</style>";

//                "}" + "*{color:#666666;font-size:"+ AutoUtils.getPercentWidthSize(12) +"px}" +

    /**
     * font txt
     */
    private static final String FORMAT = "<html> \n" +
            "<head> \n" +
            "<meta name='viewport' content='width=device-width, initial-scale=1.0,user-scalable=no'>\n" +
            "<style type=\"text/css\"> \n" +
            "body {font-size:15px;margin:0;padding:0;}\n" +
            "</style> \n" +
            "</head> \n" +
            "<body>" +
            "<script type='text/javascript'>" +
            "window.onload = function(){\n" +
            "var $img = document.getElementsByTagName('img');\n" +
            "for(var p in  $img){\n" +
            " $img[p].style.width = '100%%';\n" +
            "$img[p].style.height ='auto'\n" +
            "}\n" +

            "var $video = document.getElementsByTagName('video');\n" +
            "for(var p in  $video){\n" +
            " $video[p].style.width = '100%%';\n" +
            "$video[p].style.height ='auto'\n" +
            "}\n" +

            "}\n" +
            "</script>%1$s\n" +

//            "<script type='text/javascript'>" +
//            "window.onload = function(){\n" +
//            "var $video = document.getElementsByTagName('video');\n" +
//            "for(var p in  $video){\n" +
//            " $video[p].style.width = '100%%';\n" +
//            "$video[p].style.height ='auto'\n" +
//            "}\n" +
//            "}\n" +
//            "</script>%1$s\n" +

            "</body>\n" +
            "</html>";

    /**
     * 初始化webView的配置
     *
     * @param context
     * @param webView
     */
    @SuppressLint("JavascriptInterface")
    public static void initWebView(Activity context, WebView webView) {
//        webView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorFore));
        webView.setBackgroundColor(0); // 设置背景色
//        webView.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);//默认使用缓存
        webView.getSettings().setAllowFileAccess(true);//可以读取文件缓存(manifest生效)
        webView.getSettings().setAppCacheEnabled(false);//应用可以有缓存
        webView.getSettings().setDefaultTextEncodingName("utf-8"); // 设置编码格式
        webView.getSettings().setUseWideViewPort(true); // 是否将图片调整到适合webview的大小
        webView.getSettings().setBlockNetworkImage(false);
        webView.getSettings().setJavaScriptEnabled(true);//启用js
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//适应内容大小
//      webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//适应屏幕，内容将自动缩放
        webView.getSettings().setSaveFormData(false);//记住表单内容
        webView.getSettings().setSavePassword(false); //记住密码
        webView.getSettings().setSupportZoom(false);//支持缩放
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setHorizontalScrollBarEnabled(true); // 隐藏水平滚动条
        webView.setVerticalScrollBarEnabled(false); // 隐藏垂直滚动条
        webView.addJavascriptInterface(new JavascriptInterface(context), "appListener");
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setDomStorageEnabled(true);
//        webView.getSettings().setTextZoom(250);
//        if (SystemUtils.getDeviceModel().contains("OPPO")||SystemUtils.getDeviceModel().contains("vivo")) {
//            webView.getSettings().setTextZoom(250);
//        }
//        webView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);

    }

    /**
     * 加载url使用
     *
     * @param res
     * @return
     */
    public static String formatLinkFont(String res) {
        return FORMAT_FONT_HEAD + FORMAT_FONT_CSS + res;
    }

    /**
     * 加载富文本使用
     *
     * @param res
     * @return
     */
    public static String formatFont(String res) {
        return String.format(FORMAT, res);
    }

    /**
     * 加载富文本使用
     *
     * @param res
     * @return
     */
    public static String formatFont1(String res) {
        return FORMAT_FONT_HEAD + FORMAT_FONT_CSS + res;
    }

    /**
     * 销毁WebView，防止内存泄漏
     *
     * @param mWebView
     */
    public static void destroyWeb(WebView mWebView) {
        if (mWebView != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }
            mWebView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearHistory();
            mWebView.clearView();
            mWebView.removeAllViews();
            try {
                mWebView.destroy();
            } catch (Throwable ex) {
            }
        }
    }

    /**
     * WebViewClient监听
     */
    public static class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageFinished(view, url);
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListener(view);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);
            LogUtils.d("addImageClickListener: 开始加载");
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    /**
     * 注入js函数监听
     *
     * @param mWebView
     */
    public static void addImageClickListener(WebView mWebView) {
        LogUtils.d("addImageClickListner: 注入代码");
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，在还是执行的时候调用本地接口传递url过去
        mWebView.loadUrl("javascript:(function(){"
                + "var objs = document.getElementsByTagName(\"img\");"
                + "var imgurl=''; "
                + "for(var i=0;i<objs.length;i++)  "
                + "{"
                + "imgurl+=objs[i].src+',';"
                + "    objs[i].onclick=function()  "
                + "    {  "
                + "        appListener.openImage(imgurl);  "
                + "    }  " + "}" + "})()");
    }

    /**
     * js通信接口
     */
    public static class JavascriptInterface {

        private Activity context;

        public JavascriptInterface(Activity context) {
            this.context = context;
        }
    }
}
