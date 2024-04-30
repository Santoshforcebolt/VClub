package com.easyder.club.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;

import com.easyder.club.R;
import com.sky.widget.autolayout.utils.AutoUtils;

public class DrawableTextView extends AppCompatTextView {

    private int width;
    private int height;
    //image width、height
    private int imageWidth;
    private int imageHeight;

    private Drawable leftImage;
    private Drawable topImage;
    private Drawable rightImage;
    private Drawable bottomImage;

    private boolean isFocues;

    public DrawableTextView(Context context) {
        this(context, null);
        AutoUtils.auto(this);
    }
    
    public DrawableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        AutoUtils.auto(this);
    }

    public DrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        AutoUtils.auto(this);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomDrawableTextView, 0, 0);
        int countNum = ta.getIndexCount();
        for (int i = 0; i < countNum; i++) {

            int attr = ta.getIndex(i);
            if (attr == R.styleable.CustomDrawableTextView_leftImage) {
                leftImage = ta.getDrawable(attr);
            } else if (attr == R.styleable.CustomDrawableTextView_topImage) {
                topImage = ta.getDrawable(attr);
            } else if (attr == R.styleable.CustomDrawableTextView_rightImage) {
                rightImage = ta.getDrawable(attr);
            } else if (attr == R.styleable.CustomDrawableTextView_bottomImage) {
                bottomImage = ta.getDrawable(attr);
            } else if (attr == R.styleable.CustomDrawableTextView_imageWidth) {
                width = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_PX, 50, getResources().getDisplayMetrics()));
                imageWidth = AutoUtils.getPercentWidthSize(width);
            } else if (attr == R.styleable.CustomDrawableTextView_imageHeight) {
                height = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_PX, 50, getResources().getDisplayMetrics()));
                if (width == height) {
                    imageHeight = AutoUtils.getPercentWidthSize(height);
                } else {
                    imageHeight = AutoUtils.getPercentHeightSize(height);
                }
            }
        }

        ta.recycle();
        init();
    }

    /**
     * init views
     */
    private void init() {
        setCompoundDrawablesWithIntrinsicBounds(leftImage, topImage, rightImage, bottomImage);
    }

    public void setImagePx(int width, int height) {
        imageWidth = width;
        imageHeight = height;
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {

        if (left != null) {
            left.setBounds(0, 0, imageWidth, imageHeight);
        }

        if (top != null) {
            top.setBounds(0, 0, imageWidth, imageHeight);
        }

        if (right != null) {
            right.setBounds(0, 0, imageWidth, imageHeight);
        }

        if (bottom != null) {
            bottom.setBounds(0, 0, imageWidth, imageHeight);
        }

        setCompoundDrawables(left, top, right, bottom);
    }

    public void setDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {

        if (left != null) {
            left.setBounds(0, 0, imageWidth, imageHeight);
        }

        if (top != null) {
            top.setBounds(0, 0, imageWidth, imageHeight);
        }

        if (right != null) {
            right.setBounds(0, 0, imageWidth, imageHeight);
        }

        if (bottom != null) {
            bottom.setBounds(0, 0, imageWidth, imageHeight);
        }

        setCompoundDrawables(left, top, right, bottom);
    }


    public void resetSize(int width, int height) {
        this.width = width;
        this.height = height;
        if (width == height) {
            imageHeight = AutoUtils.getPercentWidthSize(height);
        } else {
            imageHeight = AutoUtils.getPercentWidthSize(height);
        }
        init();
    }

    public void setFocues(boolean isFocues) {
        this.isFocues = isFocues;
    }

   /* @Override
    public boolean isFocused() {

            return true;

    }*/
}

