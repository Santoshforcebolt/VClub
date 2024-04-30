package com.easyder.club.module.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperSwipeActivity;
import com.sky.wrapper.core.model.BaseVo;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author：sky on 2019/5/30 18:25.
 * Email：xcode126@126.com
 * Desc：意见反馈
 */

public class FeedbackActivity extends WrapperSwipeActivity<CommonPresenter> {

    @BindView(R.id.et_feedback)
    EditText etFeedback;

    public static Intent getIntent(Context mContext) {
        return new Intent(mContext, FeedbackActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        titleView.setTitle(getString(R.string.a_feedback));
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_FEEDBACK)) {
            showToast(getString(R.string.a_operate_success));
            this.finish();
        }
    }

    /**
     * 发起请求
     *
     * @param content
     */
    private void requestFeedBack(String content) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_FEEDBACK, new RequestParams()
                .put("content", content)
                .putCid()
                .get(), BaseVo.class);
    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        String trim = etFeedback.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            showToast(getString(R.string.a_content_not_empty));
            return;
        }
        requestFeedBack(trim);
    }
}
