package com.easyder.club.module.collect;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.collect.vo.CollectListVo;
import com.easyder.club.utils.RequestParams;
import com.easyder.club.utils.SimplePageChangeListener;
import com.gyf.immersionbar.ImmersionBar;
import com.sky.widget.component.SolveViewPager;
import com.sky.wrapper.base.adapter.TabAdapter;
import com.sky.wrapper.base.view.WrapperMvpFragment;
import com.sky.wrapper.core.model.BaseVo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/11/17 16:32
 * Email: xcode126@126.com
 * Desc: 收藏：使用子界面
 */
public class CollectFragment extends WrapperMvpFragment<CommonPresenter> {

    @BindView(R.id.tv_wine)
    TextView tvWine;
    @BindView(R.id.tv_wine_bucket)
    TextView tvWineBucket;
    @BindView(R.id.mSolveViewPager)
    SolveViewPager mSolveViewPager;

    private TabAdapter adapter;

    public static CollectFragment newInstance() {
        return new CollectFragment();
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_collect;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBar.with(_mActivity).statusBarView(R.id.status_bar_view).statusBarColor("#F5F5F5").init();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setAllSelected(0);
        initAdapter();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && presenter != null) {
            getCollectList();
        }
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
        getCollectList();
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_COLLECT_LIST)) {
            processCollectList((CollectListVo) dataVo);
        }
    }

    /**
     * @param dataVo
     */
    private void processCollectList(CollectListVo dataVo) {
        tvWine.setText(String.format("%1$s(%2$s)", getString(R.string.a_whisky), dataVo.bottlecount));
        tvWineBucket.setText(String.format("%1$s(%2$s)", getString(R.string.a_whisky_wine_bucket), dataVo.bucketcount));
    }

    /**
     * get collect list
     */
    private void getCollectList() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_COLLECT_LIST, new RequestParams()
                .putCid()
//                .put("volumetype", volumetype)
                .put("pictype", "product")
                .put("page", 1)
                .put("rows", 1)
                .get(), CollectListVo.class);
    }

    @OnClick({R.id.tv_wine, R.id.tv_wine_bucket})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_wine:
                mSolveViewPager.setCurrentItem(0);
                setAllSelected(0);
                break;
            case R.id.tv_wine_bucket:
                mSolveViewPager.setCurrentItem(1);
                setAllSelected(1);
                break;
        }
    }

    /**
     * set all select
     *
     * @param position
     */
    private void setAllSelected(int position) {
        tvWine.setSelected(position == 0);
        tvWineBucket.setSelected(position == 1);
    }

    /**
     * 初始化Adapter
     */
    private void initAdapter() {
        adapter = new TabAdapter(getChildFragmentManager(), initFragment());
        mSolveViewPager.setAdapter(adapter);
        mSolveViewPager.setOffscreenPageLimit(2);
        mSolveViewPager.addOnPageChangeListener(new SimplePageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setAllSelected(position);
            }
        });
    }

    /**
     * init fragment
     *
     * @return
     */
    private List<WrapperMvpFragment> initFragment() {
        List<WrapperMvpFragment> list = new ArrayList<>();
        list.add(CollectChildFragment.newInstance("bottle"));
        list.add(CollectChildFragment.newInstance("bucket"));
        return list;
    }
}
