package com.easyder.club.module.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.home.vo.MessageDetailVo;
import com.easyder.club.utils.RequestParams;
import com.easyder.club.utils.WebViewUtils;
import com.easyder.club.widget.RollingWebView;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;

import butterknife.BindView;

/**
 * Author: sky on 2020/11/23 16:36
 * Email: xcode126@126.com
 * Desc:
 */
public class MessageDetailActivity extends WrapperStatusActivity<CommonPresenter> {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_visitor)
    TextView tvVisitor;
    @BindView(R.id.mRollingWebView)
    RollingWebView mRollingWebView;

    private int id;
    private String masterid;

    public static Intent getIntent(Context context, int id, String masterid) {
        return new Intent(context, MessageDetailActivity.class)
                .putExtra("id", id).putExtra("masterid", masterid);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_message_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        titleView.setTitle(getString(R.string.a_msg_detail));
        id = intent.getIntExtra("id", 0);
        masterid = intent.getStringExtra("masterid");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mRollingWebView.setLayoutParams(params);
        mRollingWebView.setOnCustomScrollChangeListener((l, t, oldl, oldt) -> {

        });
        WebViewUtils.initWebView(mActivity, mRollingWebView);
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        getDetail();
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_MESSAGE_DETAIL)) {
            processDetail((MessageDetailVo) dataVo);
        }
    }

    /**
     * get detail
     */
    private void getDetail() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_MESSAGE_DETAIL, new RequestParams()
                .put("id", id)
                .put("masterid", masterid)
                .get(), MessageDetailVo.class);
    }

    /**
     * @param dataVo
     */
    private void processDetail(MessageDetailVo dataVo) {
        tvTitle.setText(dataVo.title);
        tvVisitor.setText(dataVo.gmtmodified);
        mRollingWebView.loadData(WebViewUtils.formatFont(dataVo.content), "text/html; charset=UTF-8", null);
    }

}
