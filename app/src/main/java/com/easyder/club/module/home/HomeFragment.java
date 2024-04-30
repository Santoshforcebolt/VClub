package com.easyder.club.module.home;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.ApiConfig;
import com.easyder.club.AppConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.home.adapter.HomeAdapter;
import com.easyder.club.module.home.vo.NewsListVo;
import com.easyder.club.utils.OperateUtils;
import com.easyder.club.utils.RequestParams;
import com.gyf.immersionbar.ImmersionBar;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.view.WrapperDialog;
import com.sky.wrapper.base.view.WrapperStatusFragment;
import com.sky.wrapper.core.manager.ImageManager;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;
import com.sky.wrapper.utils.LanguageUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.jzvd.JZVideoPlayer;


/**
 * Author: sky on 2020/11/12 17:46.
 * Email:xcode126@126.com
 * Desc: 首页
 */

public class HomeFragment extends WrapperStatusFragment<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.ll_language)
    LinearLayout llLanguage;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private HomeAdapter homeAdapter;
    private int page, totalPage;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBar.with(_mActivity).statusBarView(R.id.status_bar_view).statusBarColor(R.color.colorFore).init();
    }

    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        homeAdapter = new HomeAdapter();
        mRecyclerView.setAdapter(homeAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
        homeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewsListVo.ArticlelistBean item = homeAdapter.getItem(position);
                startActivity(NewsDetailActivity.getIntent(_mActivity, item.id+""));
            }
        });
        llLanguage.setSelected(!LanguageUtils.isEnglishLanguage(_mActivity));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && presenter != null) {
            onRefresh();
        }
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
        onRefresh();
    }

    @Override
    public void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onDestroyView() {
        ImmersionBar.with(_mActivity).destroy(this);
        JZVideoPlayer.setJzUserAction(null);
        super.onDestroyView();
    }

    @Override
    public boolean onBackPressedSupport() {
        if (JZVideoPlayer.backPress()) {
            return true;
        }
        return super.onBackPressedSupport();
    }

    @Override
    public void onRefresh() {
        getList(page = 1);
    }

    @Override
    public void onLoadMoreRequested() {
        if (page < totalPage) {
            getList(++page);
        } else {
            homeAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_NEWS_LIST)) {
            processNewsList((NewsListVo) dataVo);
        }
    }

    /**
     * get list
     *
     * @param page
     */
    private void getList(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_NEWS_LIST, new RequestParams()
                .putCid()
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .get(), NewsListVo.class);
    }

    /**
     * process news list
     *
     * @param dataVo
     */
    private void processNewsList(NewsListVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total <= 0) {
                homeAdapter.getData().clear();
                homeAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                homeAdapter.notifyDataSetChanged();
            } else {
                homeAdapter.setNewData(dataVo.articlelist);
            }
            if (homeAdapter.getHeaderLayoutCount() > 0) {
                homeAdapter.removeAllHeaderView();
                homeAdapter.notifyDataSetChanged();
            }
            homeAdapter.setHeaderView(getHeaderView(dataVo.newsBannerList));
            mNestedRefreshLayout.refreshFinish();
        } else {
            homeAdapter.addData(dataVo.articlelist);
            homeAdapter.loadMoreComplete();
        }
    }

    @OnClick({R.id.ll_language, R.id.fl_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_language:
                switchLanguageDialog();
                break;
            case R.id.fl_message:
                startActivity(MessageListActivity.getIntent(_mActivity));
                break;
        }
    }

    /**
     * get header view
     *
     * @param list
     * @return
     */
    private View getHeaderView(List<NewsListVo.NewsBannerListBean> list) {
        return getHelperView(mRecyclerView, R.layout.header_home, helper -> {
            BGABanner mBGABanner = helper.getView(R.id.mBGABanner);
            mBGABanner.setAdapter((BGABanner.Adapter<ImageView, NewsListVo.NewsBannerListBean>) (banner, itemView, item, position)
                    -> ImageManager.load(_mActivity, itemView, item.bannerimg, R.drawable.ic_placeholder_2, R.drawable.ic_placeholder_2));
            mBGABanner.setDelegate((BGABanner.Delegate<ImageView, NewsListVo.NewsBannerListBean>) (banner, itemView, item, position) -> {
                if (!TextUtils.isEmpty(item.jumpid)) {
                    startActivity(NewsDetailActivity.getIntent(_mActivity, item.jumpid));
                }
            });
            mBGABanner.setData(list, null);
        });
    }

    /**
     * 切换语言弹窗
     */
    private void switchLanguageDialog() {
        new WrapperDialog(_mActivity) {
            @Override
            public int getLayoutRes() {
                return R.layout.dialog_switch_language;
            }

            @Override
            protected void setDialogParams(Dialog dialog) {
                setDialogParams(dialog, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            }

            @Override
            public void help(ViewHelper helper) {
                helper.getView(R.id.tv_chinese).setSelected(!LanguageUtils.isEnglishLanguage(_mActivity));
                helper.getView(R.id.tv_english).setSelected(LanguageUtils.isEnglishLanguage(_mActivity));
                helper.getView(R.id.tv_chinese).setEnabled(LanguageUtils.isEnglishLanguage(_mActivity));
                helper.getView(R.id.tv_english).setEnabled(!LanguageUtils.isEnglishLanguage(_mActivity));
                helper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.tv_chinese:
                            case R.id.tv_english:
                                OperateUtils.switchLanguage(_mActivity);
                                break;
                        }
                        dismiss();
                    }
                }, R.id.iv_close, R.id.tv_chinese, R.id.tv_english);
            }
        }.show();
    }

}
