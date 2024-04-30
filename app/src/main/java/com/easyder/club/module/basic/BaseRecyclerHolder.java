package com.easyder.club.module.basic;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sky.widget.autolayout.utils.AutoUtils;
import com.sky.wrapper.core.manager.ImageManager;


/**
 * Auther:  winds
 * Data:    2017/5/9
 * Desc:
 */

public class BaseRecyclerHolder extends BaseViewHolder {

    public BaseRecyclerHolder(View view) {
        super(view);
        AutoUtils.auto(view);
    }


    public BaseRecyclerHolder setVisibleText(int viewId, CharSequence msg) {
        TextView view = getView(viewId);
        view.setVisibility(View.VISIBLE);
        view.setText(msg);
        return this;
    }

    public BaseRecyclerHolder setBackground(Context context, int viewId, Object url) {
        return setBackground(context, viewId, url, -1);
    }

    public BaseRecyclerHolder setBackground(Context context, int viewId, Object url, int placeholder) {
        return setBackground(context, viewId, url, placeholder, -1);
    }

    public BaseRecyclerHolder setBackground(Context context, int viewId, Object url, int placeholder, int error) {
        return setBackground(context, viewId, url, null, placeholder, error);
    }

    public BaseRecyclerHolder setBackground(Context context, int viewId, Object url, BitmapTransformation transformation, int placeholder, int error) {
        View view = getView(viewId);
        ImageManager.load(context, url, placeholder, error, transformation, view);
        return this;
    }


    public BaseRecyclerHolder setImageManager(Context context, int viewId, String url) {
        return setImageManager(context, viewId, url, -1);
    }

    public BaseRecyclerHolder setImageManager(Context context, int viewId, String url, int placeholder) {
        return setImageManager(context, viewId, url, placeholder, -1);
    }

    public BaseRecyclerHolder setImageManager(Context context, int viewId, String url, int placeholder, int error) {
        return setImageManager(context, viewId, url, null, placeholder, error);
    }

    public BaseRecyclerHolder setImageManager(Context context, int viewId, String url, BitmapTransformation transformation, int placeholder, int error) {
        ImageView view = getView(viewId);
        ImageManager.load(context, view, url, transformation, placeholder, error);
        return this;
    }

    public BaseRecyclerHolder setImageManager(Context context, ImageView imageView, String url, BitmapTransformation transformation, int placeholder, int error) {
        ImageManager.load(context, imageView, url, transformation, placeholder, error);
        return this;
    }
    public void setImageManager(Context context, ImageView imageView, String url, int placeholder, int error) {
        ImageManager.load(context, imageView, url, placeholder, error);
    }
    /**
     * 显示带删除线字体
     *
     * @param viewId
     * @param text
     */
    public void setTextDelete(int viewId, CharSequence text) {
        TextView view = getView(viewId);
        view.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);//删除线
        view.setText(text);
    }
}
