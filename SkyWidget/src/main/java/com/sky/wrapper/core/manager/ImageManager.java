package com.sky.wrapper.core.manager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sky.wrapper.ManagerConfig;


/**
 * 本地及远程服务器的图片管理
 *
 * @author 刘琛慧
 * date 2016/6/20.
 */
public class ImageManager {

    private static ImageManager instance;

    private ImageManager() {

    }

    public static ImageManager getDefault() {
        if (instance == null) {
            synchronized (ImageManager.class) {
                instance = new ImageManager();
            }
            return instance;
        }
        return instance;
    }


    /**
     * 加载指定URL的图片并设置到targetView
     *
     * @param context
     * @param obj
     * @param targetView
     */
    public static void load(Context context, Object obj, final View targetView) {
        load(context, obj, -1, targetView);
    }

    public static void load(Context context, Object obj, int place, final View targetView) {
        load(context, obj, place, null, targetView);
    }

    public static void load(Context context, Object obj, int place, BitmapTransformation transformation, final View targetView) {
        load(context, obj, place, -1, transformation, targetView);
    }

    public static void load(Context context, Object obj, int place, int error, BitmapTransformation transformation, final View targetView) {
        RequestOptions options = new RequestOptions();
        if (place != 0 && place != -1) {
            options.placeholder(place);
        }
        if (transformation != null) {
            options.transform(transformation);
        }

        if (error != 0 && error != -1) {
            options.error(error);
        }

        Glide.with(context).load(obj).apply(options).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (targetView instanceof ImageView) {
                    ((ImageView) targetView).setImageDrawable(resource);
                } else {
                    targetView.setBackground(resource);
                }
            }
        });
    }

    public static void load(Context context, ImageView view, String url) {
        load(context, view, url, -1);
    }

    /**
     * 初始化占位图，加载指定URL的图片并设置到ImageView
     *
     * @param context
     * @param view
     * @param url
     * @param placeholder
     */
    public static void load(Context context, ImageView view, String url, int placeholder) {
        load(context, view, url, placeholder, -1);
    }

    public static void load(Context context, ImageView view, String url, int placeholder, int error) {
        load(context, view, url, null, placeholder, error);
    }

    /**
     * 加载图片
     *
     * @param view
     * @param url
     * @param placeholder 值为 -1 / 0 表示不加载
     * @param error       值为 -1 / 0 表示不加载
     */
    public static void load(Context context, ImageView view, String url, BitmapTransformation transformation, int placeholder, int error) {
        load(Glide.with(context), view, url, transformation, placeholder, error);
    }

    /**
     * 加载图片
     *
     * @param manager
     * @param view
     * @param url
     * @param placeholder 值为 -1 / 0 表示不加载
     * @param error       值为 -1 / 0 表示不加载
     */
    public static void load(RequestManager manager, ImageView view, String url, BitmapTransformation transformation, int placeholder, int error) {
        if (url == null) {
            url = "";
        }
        if (!url.startsWith("http")) {
            url = String.format("%1$s%2$s", ManagerConfig.getInstance().getBaseHost(), url.startsWith("/") ? url.substring(1, url.length()) : url);
        }
        RequestOptions options = new RequestOptions();

        if (transformation != null) {
            options.transform(transformation);
        }
//        if (placeholder == 0 || placeholder == -1) {
//            placeholder = R.drawable.ic_placeholder_1;
//        }
        options.placeholder(placeholder);
//        if (error == 0 || error == -1) {
//            error = R.drawable.ic_placeholder_1;
//        }
        options.error(error);

//        if (placeholder != 0 && placeholder != -1) {
//            options.placeholder(placeholder);
//        }
//        if (error != 0 && error != -1) {
//            options.error(error);
//        }
        manager.load(url).apply(options).into(view);
    }


    /**
     * 加载视频第一帧做封面图
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadVideoImage(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).frame(1000000).centerCrop();
//        options.error(R.drawable.ic_placeholder_1).placeholder(R.drawable.ic_placeholder_1);
        Glide.with(imageView)
                .setDefaultRequestOptions(options)
//                .load(Constant.getResourceUrl(url))
                .load(url)
                .into(imageView);
    }


}
