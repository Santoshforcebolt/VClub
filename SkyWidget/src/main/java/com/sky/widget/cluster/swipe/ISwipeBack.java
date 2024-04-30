package com.sky.widget.cluster.swipe;

/**
 * Author: sky on 2021/1/6 15:36
 * Email: xcode126@126.com
 * Desc: ISwipeBack
 */

public interface ISwipeBack {
    /**
     * @return the SwipeBackLayout associated with this activity.
     */
    SwipeBackLayout getSwipeBackLayout();

    void setSwipeBackEnable(boolean enable);

    /**
     * Scroll out contentView and finish the activity
     */
    void scrollToFinishActivity();
}
