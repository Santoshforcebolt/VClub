package com.sky.widget.cluster.refresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sky.widget.R;
import com.sky.widget.autolayout.utils.AutoUtils;

public class SkyRefreshView extends LinearLayout implements IPullRefreshView {

    private AnimationDrawable animationDrawable;
    ImageView progress;

    public SkyRefreshView(Context context) {
        this(context, null);
    }

    public SkyRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);
        FrameLayout layout = new FrameLayout(context);
        LayoutParams parentParams = new LayoutParams(AutoUtils.getPercentWidthSize(194),AutoUtils.getPercentWidthSize(194));
        progress = new ImageView(context);
        FrameLayout.LayoutParams progressParams = new FrameLayout.LayoutParams(AutoUtils.getPercentWidthSize(194),AutoUtils.getPercentWidthSize(194));
        progressParams.gravity = Gravity.CENTER;
        layout.addView(progress, progressParams);
        animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.ic_refresh_sky);
        progress.setImageDrawable(animationDrawable);
        progress.setVisibility(INVISIBLE);

        addView(layout, parentParams);
    }

    //隐藏
    @Override
    public void onPullHided() {
        progress.setVisibility(INVISIBLE);
        progress.clearAnimation();
        progress.setVisibility(INVISIBLE);
    }

    //刷新中
    @Override
    public void onPullRefresh() {
        progress.setVisibility(VISIBLE);
    }

    //提示松手
    @Override
    public void onPullFreeHand() {
        progress.setVisibility(VISIBLE);
    }

    //下啦中
    @Override
    public void onPullDowning() {
        progress.setVisibility(VISIBLE);
        animationDrawable.start();
    }

    //刷新完成
    @Override
    public void onPullFinished() {
        animationDrawable.stop();
        progress.setVisibility(VISIBLE);
    }

    @Override
    public void onPullProgress(float pullDistance, float pullProgress) {

    }
}
