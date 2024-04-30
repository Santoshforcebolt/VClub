package com.sky.widget.cluster.tabbar;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import androidx.core.view.ViewCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: sky on 2021/1/6 15:36
 * Email: xcode126@126.com
 * Desc: TabBar
 */
public class TabBar extends LinearLayout {
    private static final int TRANSLATE_DURATION_MILLIS = 200;

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private boolean mVisible = true;

    private List<TabItem> mTabs = new ArrayList<>();

    private LinearLayout mTabLayout;

    private LayoutParams mTabParams;
    private int mCurrentPosition = 0;
    private OnTabSelectedListener mListener;

    public TabBar(Context context) {
        this(context, null);
    }

    public TabBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        setBackgroundColor(Color.parseColor("#FFFFFF"));
        mTabLayout = new LinearLayout(context);
        mTabLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(mTabLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mTabParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        mTabParams.weight = 1;
    }

    public TabBar addItem(final TabItem tab) {
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    int pos = tab.getTabPosition();
                    if (mCurrentPosition == pos) {
                        mListener.onTabReSelected(pos);
                    } else {
                        if (!mListener.beforeTabSelected(pos, mCurrentPosition)) {

                            if (linkView != null && linkPos == pos && jumpable) {
                                mListener.onTabSelected(pos, mCurrentPosition);

                            }else {
                                mListener.onTabSelected(pos, mCurrentPosition);
                                if (linkView != null && linkPos == pos) { //判断有没有需要依附的View
                                    linkView.setSelected(true);
                                } else {
                                    tab.setSelected(true);
                                }
                                mListener.onTabUnSelected(mCurrentPosition);

                                if (linkView != null && linkPos == mCurrentPosition) {
                                    linkView.setSelected(false);
                                } else {
                                    mTabs.get(mCurrentPosition).setSelected(false);
                                }

                                mCurrentPosition = pos;
                            }
                        }
                    }
                }
            }
        });
        tab.setTabPosition(mTabLayout.getChildCount());
        tab.setLayoutParams(mTabParams);
        mTabLayout.addView(tab);
        mTabs.add(tab);
        return this;
    }

    boolean jumpable;
    View linkView;
    int linkPos;

    /**
     * 设置依附的View 需要设置个空View用于占位依附
     *
     * @param linkView
     * @param linkPos
     * @return
     */
    public TabBar link(View linkView, final int linkPos) {
       return link(linkView, linkPos, false);
    }


    /**
     * 设置依附的View 需要设置个空View用于占位依附
     *
     * @param linkView
     * @param linkPos
     * @param jumpable 点击依附的View是否切换页面   true 不选中，避免选中效果  用于跳转新页面而不是切换tab
     * @return
     */
    public TabBar link(View linkView, final int linkPos, boolean jumpable) {
        this.linkView = linkView;
        this.linkPos = linkPos;
        this.jumpable = jumpable;
        linkView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentItem(linkPos);
            }
        });
        return this;
    }

    public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        mListener = onTabSelectedListener;
    }

    public void setCurrentItem(final int position) {
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                mTabLayout.getChildAt(position).performClick();
            }
        });
    }

    public int getCurrentItemPosition() {
        return mCurrentPosition;
    }

    /**
     * 获取 Tab
     */
    public TabItem getItem(int index) {
        if (mTabs.size() < index) return null;
        return mTabs.get(index);
    }

    public int getCount() {
        return mTabs.size();
    }

    public interface OnTabSelectedListener {
        /**
         * Tab被选中前回调，若返回true，则可消费掉当前事件
         *
         * @param position
         * @return
         */
        boolean beforeTabSelected(int position, int prePosition);

        /**
         * 当前Tab被选中时的回调，可用于页面切换
         *
         * @param position
         * @param prePosition
         */
        void onTabSelected(int position, int prePosition);

        /**
         * 当前Tab被切换时的上一个被选中的位置
         *
         * @param position
         */
        void onTabUnSelected(int position);

        /**
         * 当前Tab已被选中，再次点击时的回调
         *
         * @param position
         */
        void onTabReSelected(int position);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, mCurrentPosition);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        if (mCurrentPosition != ss.position) {
            mTabLayout.getChildAt(mCurrentPosition).setSelected(false);
            mTabLayout.getChildAt(ss.position).setSelected(true);
        }
        mCurrentPosition = ss.position;
    }

    static class SavedState extends BaseSavedState {
        private int position;

        public SavedState(Parcel source) {
            super(source);
            position = source.readInt();
        }

        public SavedState(Parcelable superState, int position) {
            super(superState);
            this.position = position;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(position);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }


    public void hide() {
        hide(true);
    }

    public void show() {
        show(true);
    }

    public void hide(boolean anim) {
        toggle(false, anim, false);
    }

    public void show(boolean anim) {
        toggle(true, anim, false);
    }

    public boolean isVisible() {
        return mVisible;
    }

    private void toggle(final boolean visible, final boolean animate, boolean force) {
        if (mVisible != visible || force) {
            mVisible = visible;
            int height = getHeight();
            if (height == 0 && !force) {
                ViewTreeObserver vto = getViewTreeObserver();
                if (vto.isAlive()) {
                    // view树完成测量并且分配空间而绘制过程还没有开始的时候播放动画。
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            ViewTreeObserver currentVto = getViewTreeObserver();
                            if (currentVto.isAlive()) {
                                currentVto.removeOnPreDrawListener(this);
                            }
                            toggle(visible, animate, true);
                            return true;
                        }
                    });
                    return;
                }
            }
            int translationY = visible ? 0 : height;
            if (animate) {
                animate().setInterpolator(mInterpolator)
                        .setDuration(TRANSLATE_DURATION_MILLIS)
                        .translationY(translationY);
            } else {
                ViewCompat.setTranslationY(this, translationY);
            }
        }
    }
}
