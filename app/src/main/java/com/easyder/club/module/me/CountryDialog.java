package com.easyder.club.module.me;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easyder.club.R;
import com.easyder.club.module.me.adapter.CountryAdapter;
import com.easyder.club.module.me.vo.CountryListVo;
import com.easyder.club.widget.SlideBar;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.view.WrapperDialog;

/**
 * Author: sky on 2020/12/2 17:08
 * Email: xcode126@126.com
 * Desc:
 */
public class CountryDialog extends WrapperDialog {

    public CountryDialog(Context context, CountryListVo listVo, OnSelectListener listener) {
        super(context);
        this.listener = listener;
        initView(listVo);
    }

    private void initView(CountryListVo listVo) {
        setHelperCallback(new HelperCallback() {
            @Override
            public void help(Dialog dialog, ViewHelper helper) {
                SlideBar mSlideBar = helper.getView(R.id.mSlideBar);
                RecyclerView mRecyclerView = helper.getView(R.id.mRecyclerView);
                CountryAdapter countryAdapter = new CountryAdapter();
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                mRecyclerView.setAdapter(countryAdapter);
                if (listVo != null && listVo.rows != null && listVo.rows.size() > 0) {
                    countryAdapter.operateData(listVo.rows);
                }
                countryAdapter.setOnItemClickListener((adapter, view, position) -> {
                    CountryListVo.RowsBean item = countryAdapter.getItem(position);
                    if (listener != null) {
                        listener.onResult(item);
                        dismiss();
                    }
                });
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (mShouldScroll && RecyclerView.SCROLL_STATE_IDLE == newState) {
                            mShouldScroll = false;
                            smoothMoveToPosition(mRecyclerView, mToPosition);
                        }
                    }
                });
                mSlideBar.setOnTouchingLetterChangedListener((s, isUp) -> {
                    if (isUp) {
//                    tvKeyword.setVisibility(View.GONE);
                    } else {
//                    tvKeyword.setVisibility(View.VISIBLE);
                        for (int i = 0; i < countryAdapter.getItemCount(); i++) {
                            CountryListVo.RowsBean item = countryAdapter.getItem(i);
                            if (item != null && !TextUtils.isEmpty(item.getPinyin())) {
                                String word = item.getPinyin().substring(0, 1);
                                if (word.equals(s)) {
                                    smoothMoveToPosition(mRecyclerView, i);
                                    return;
                                }
                            }
                        }
//                    tvKeyword.setText(s);
                    }
                });
            }
        });
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_country;
    }

    @Override
    protected void setDialogParams(Dialog dialog) {
        setDialogAbsParams(dialog, 620, 1050, Gravity.CENTER);
    }

    @Override
    public void help(ViewHelper helper) {
        helper.setOnClickListener(R.id.iv_close, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;

    /**
     * 滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前，使用smoothScrollToPosition
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后，最后一个可见项之前
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                // smoothScrollToPosition 不会有效果，此时调用smoothScrollBy来滑动到指定位置
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }

    public CountryDialog.OnSelectListener listener;

    public interface OnSelectListener {
        void onResult(CountryListVo.RowsBean item);
    }


}
