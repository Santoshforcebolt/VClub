package com.easyder.club.module.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.home.vo.NewsDetailVo;
import com.easyder.club.utils.RequestParams;
import com.easyder.club.utils.WebViewUtils;
import com.easyder.club.widget.RollingWebView;
import com.sky.widget.autolayout.utils.AutoUtils;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Author: sky on 2020/11/23 17:15
 * Email: xcode126@126.com
 * Desc:
 */
public class NewsDetailActivity extends WrapperStatusActivity<CommonPresenter> {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_visitor)
    TextView tvVisitor;
    @BindView(R.id.mRollingWebView)
    RollingWebView mRollingWebView;
    @BindView(R.id.ll_like)
    LinearLayout llLike;
    @BindView(R.id.tv_num)
    TextView tvNum;

    private String id;

    public static Intent getIntent(Context context, String id) {
        return new Intent(context, NewsDetailActivity.class).putExtra("id", id);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_news_detail;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        titleView.setTitle(getString(R.string.a_details));
        id = intent.getStringExtra("id");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin= AutoUtils.getPercentWidthSize(30);
        params.rightMargin= AutoUtils.getPercentWidthSize(30);
        mRollingWebView.setLayoutParams(params);
        mRollingWebView.setOnCustomScrollChangeListener((l, t, oldl, oldt) -> {

        });
        WebViewUtils.initWebView(mActivity, mRollingWebView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getStatusDelegate().removeStatus();
            }
        }, 1500);
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        getDetail();
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
//        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_NEWS_DETAIL)) {
            processNewsDetail((NewsDetailVo) dataVo);
        } else if (url.contains(ApiConfig.API_NEWS_LIKE)) {
            getDetail();
        }
    }

    /**
     * get detail
     */
    private void getDetail() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_NEWS_DETAIL, new RequestParams()
                .putCid()
                .put("id", id)
                .get(), NewsDetailVo.class);
    }

    /**
     * like news
     */
    private void likeNews() {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_NEWS_LIKE, new RequestParams()
                .putCid()
                .put("articleid", id)
                .get(), BaseVo.class);
    }

    /**
     * @param dataVo
     */
    private void processNewsDetail(NewsDetailVo dataVo) {
        if (dataVo.article != null) {
            NewsDetailVo.ArticleBean article = dataVo.article;
            tvTitle.setText(article.title);
            tvVisitor.setText(String.format("%1$s%2$s%3$s %4$s", article.views, getString(R.string.a_unit_ci), getString(R.string.a_look), article.releasetime));
            mRollingWebView.loadData(WebViewUtils.formatFont(article.content), "text/html; charset=UTF-8", null);
            llLike.setSelected(article.ipraise == 1);
            tvNum.setText(String.valueOf(article.praisenum));
        }
    }

    @OnClick(R.id.ll_like)
    public void onViewClicked() {
        likeNews();
    }
}
