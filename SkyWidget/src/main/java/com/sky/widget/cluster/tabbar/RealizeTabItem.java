package com.sky.widget.cluster.tabbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sky.widget.R;
/**
 * Author: sky on 2021/1/6 15:36
 * Email: xcode126@126.com
 * Desc: TabItem的实现类
 */
public class RealizeTabItem extends TabItem {
    ImageView image;
    TextView txt;
    TextView msg;
    View dot;

    int position = -1;
    public RealizeTabItem(@NonNull Context context) {
        this(context, null);
    }

    public RealizeTabItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RealizeTabItem(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_normal_tab, this, true);
        image = (ImageView) findViewById(R.id.image);
        txt = (TextView) findViewById(R.id.txt);
        msg = (TextView) findViewById(R.id.msg);
        dot = findViewById(R.id.dot);

        TypedArray typedArray = context.obtainStyledAttributes(new int[]{R.attr.selectableItemBackgroundBorderless});
        Drawable drawable = typedArray.getDrawable(0);
        setBackgroundDrawable(drawable);
        typedArray.recycle();
    }

    public RealizeTabItem init(@DrawableRes int icon, @NonNull String title) {
        image.setImageResource(icon);
        txt.setText(title);
        return this;
    }


    @Override
    public void setSelected(boolean selected) {
        image.setSelected(selected);
        txt.setSelected(selected);
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
    public void setMessage(int num) {
        if (num > 0) {
            msg.setVisibility(VISIBLE);
            if (num < 100) {
                msg.setText(String.valueOf(num));
            } else {
                msg.setText(String.format("%1$d+", 99));
            }
        }
    }

    @Override
    public void setDot(boolean visible) {
        dot.setVisibility(visible ? VISIBLE : GONE);
    }


    public String getTitle() {
        return txt.getText().toString();
    }


    @Override
    public void dismiss() {
        dot.setVisibility(GONE);
        msg.setVisibility(GONE);
    }
}
