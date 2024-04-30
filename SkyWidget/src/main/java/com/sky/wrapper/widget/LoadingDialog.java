package com.sky.wrapper.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.sky.widget.R;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.view.WrapperDialog;
import com.sky.wrapper.utils.UIUtils;

/**
 * Auther:  winds
 * Data:    2018/5/3
 * Version: 1.0
 * Desc:
 */


public class LoadingDialog extends WrapperDialog {


    private AnimationDrawable animationDrawable;

    public LoadingDialog(Context context) {
        super(context);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_loading;
    }

    @Override
    protected void setDialogParams(Dialog dialog) {
        setDialogParams(dialog, Gravity.CENTER);
//        setOrientation(LinearLayout.VERTICAL);
    }

    @Override
    public void help(ViewHelper helper) {
        helper.setBackgroundDrawable(R.id.loading_view, new ColorDrawable(Color.parseColor("#FFFFFF"))
                .setRoundCorner(UIUtils.dip2px(5), UIUtils.dip2px(5)));
        ProgressBar progressBar = helper.getView(R.id.loading_progressbar);
//        progressBar.setIndeterminateDrawable(UIUtils.getDrawable(R.drawable.ic_refresh_sky));


//        FrameLayout layoutContainer = helper.getView(R.id.loading_view);
//        FrameLayout layout = new FrameLayout(context);
//        LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(AutoUtils.getPercentWidthSize(194),AutoUtils.getPercentWidthSize(194));
//        ImageView progress = new ImageView(context);
//        FrameLayout.LayoutParams progressParams = new FrameLayout.LayoutParams(AutoUtils.getPercentWidthSize(194),AutoUtils.getPercentWidthSize(194));
//        progressParams.gravity = Gravity.CENTER;
//        layout.addView(progress, progressParams);
//        animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.ic_refresh_sky);
//        progress.setImageDrawable(animationDrawable);
////        progress.setVisibility(INVISIBLE);
//        layoutContainer.addView(layout, parentParams);
    }

    public LoadingDialog setOrientation(int orientation) {
        if (helper != null) {
            ((LinearLayout) helper.getView(R.id.loading_view)).setOrientation(orientation);
            if (orientation == LinearLayout.HORIZONTAL) {
                helper.getView(R.id.loading_message).setPadding(UIUtils.dip2px(8), 0, 0, 0);
            } else {
                helper.getView(R.id.loading_message).setPadding(0, UIUtils.dip2px(8), 0, 0);
            }
        }
        return this;
    }

//    @Override
//    public WrapperDialog dismiss() {
//        if (null != animationDrawable && animationDrawable.isRunning()) {
//            animationDrawable.stop();
//        }
//        return super.dismiss();
//    }
}
