package com.sky.widget.cluster.refresh;

/**
 * Author: sky on 2021/1/6 15:36
 * Email: xcode126@126.com
 * Desc: IPullRefreshView
 */

public interface IPullRefreshView {

  enum State{
    GONE,MOVE_PULL,MOVE_WAIT_REFRESH,MOVE_REFRESH,MOVE_SRPINGBACK;
  }

  /**
   * 隐藏
   */
  void onPullHided();

  /**
   * 刷新中
   */
  void onPullRefresh();

  /**
   * 提示松手
   */
  void onPullFreeHand();

  /**
   * 下啦中
   */
  void onPullDowning();

  /**
   * 刷新完成
   */
  void onPullFinished();

  /**
   * @param pullDistance 下拉的距离
   * @param pullProgress 下拉的距离的比例
   */
  void onPullProgress(float pullDistance, float pullProgress);
}
