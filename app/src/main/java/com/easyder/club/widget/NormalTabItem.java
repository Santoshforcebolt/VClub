package com.easyder.club.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.easyder.club.R;
import com.sky.widget.cluster.tabbar.TabItem;


/**
 * Author:  winds
 * Data:    2017/9/14
 * Version: 1.0
 * Desc:
 */


public class NormalTabItem extends TabItem {
    int position = -1;
    ImageView iv_image;
    TextView txt;
    TextView msg;

    public NormalTabItem(@NonNull Context context) {
        this(context, null);
    }

    public NormalTabItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NormalTabItem(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_normal_tab_item, this, true);
        iv_image = findViewById(R.id.image);
        txt = findViewById(R.id.txt);
        msg = findViewById(R.id.msg);
    }

    @Override
    public void setTabPosition(int position) {
        this.position = position;
        if (position == 0) {
            setSelected(true);
        }
    }

    @Override
    public int getTabPosition() {
        return position;
    }

    @Override
    public void setSelected(boolean selected) {
        iv_image.setSelected(selected);
        txt.setSelected(selected);
    }

    @Override
    public void setMessage(int num) {
        msg.setVisibility(num != 0 ? View.VISIBLE : GONE);
        msg.setText(String.valueOf(num));
    }

    @Override
    public void setDot(boolean visible) {

    }


    public String getTitle() {
        return null;
    }

    @Override
    public void dismiss() {

    }

    public NormalTabItem init(@DrawableRes int icon) {
        iv_image.setImageResource(icon);
        return this;
    }

    public NormalTabItem init(@DrawableRes int icon, String name) {
        iv_image.setImageResource(icon);
        txt.setText(name);
        return this;
    }
}
