package com.easyder.club.module.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.shop.vo.LimitBuyVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.autolayout.utils.AutoUtils;
import com.sky.widget.component.SolveViewPager;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.adapter.TabAdapter;
import com.sky.wrapper.base.view.WrapperMvpFragment;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
/**
 * Author: sky on 2020/11/25 16:29
 * Email: xcode126@126.com
 * Desc: 限时抢购
 */
public class LimitBuyActivity extends WrapperStatusActivity<CommonPresenter> {

    @BindView(R.id.mIndicator)
    MagicIndicator mIndicator;
    @BindView(R.id.mViewPager)
    SolveViewPager mViewPager;

    private TabAdapter tabAdapter;
    private CommonNavigator titleNavigator;
    private FragmentContainerHelper subtitleContainerHelper;
    private int activitycode, index;

    public static Intent getIntent(Context context, int activitycode) {
        return new Intent(context, LimitBuyActivity.class).putExtra("activitycode", activitycode);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.common_indicator;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        titleView.setTitle(getString(R.string.a_limit_buy));
        activitycode = intent.getIntExtra("activitycode", 0);
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        getList();
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_LIMIT_BUY)) {
            processList((LimitBuyVo) dataVo);
        }
    }

    /**
     * get list
     */
    private void getList() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_LIMIT_BUY, new RequestParams()
                .putCid()
                .get(), LimitBuyVo.class);
    }

    /**
     * @param buyVo
     */
    private void processList(LimitBuyVo buyVo) {
        List<WrapperMvpFragment> fragments = initFragments(buyVo.activityTags);
        tabAdapter = new TabAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(tabAdapter);
        mViewPager.setOffscreenPageLimit(fragments.size());
        initTitleIndicator(buyVo.activityTags);
    }

    /**
     * @param list
     * @return
     */
    private List<WrapperMvpFragment> initFragments(List<LimitBuyVo.ActivityTagsBean> list) {
        List<WrapperMvpFragment> pageList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                LimitBuyVo.ActivityTagsBean bean = list.get(i);
                pageList.add(LimitBuyFragment.newInstance(bean.activitycode));
                if (bean.activitycode == activitycode) {
                    index = i;
                }
            }
        }
        return pageList;
    }

    /**
     * 初始化菜单栏
     */
    private void initTitleIndicator(List<LimitBuyVo.ActivityTagsBean> list) {
        titleNavigator = new CommonNavigator(mActivity);
        titleNavigator.setAdjustMode(true);//true 自适应居中、false 反之
        titleNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return list != null ? list.size() : 0;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                LimitBuyVo.ActivityTagsBean bean = list.get(index);
                simplePagerTitleView.setText(bean != null ? bean.activitytag : "");
                int width = AutoUtils.getPercentWidthSize(20);
                simplePagerTitleView.setPadding(width, 0, width, 0);
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentHeightSize(28));
                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.textLesser));
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.colorRed));

                simplePagerTitleView.setOnClickListener(v -> mViewPager.setCurrentItem(index));
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setColors(ContextCompat.getColor(mActivity, R.color.white));

                WrapPagerIndicator indicator = new WrapPagerIndicator(context);
                indicator.setFillColor(ContextCompat.getColor(mActivity, R.color.colorBrown));
                return linePagerIndicator;
            }
        });
        mIndicator.setNavigator(titleNavigator);
        ViewPagerHelper.bind(mIndicator, mViewPager);
        mViewPager.setCurrentItem(index, false);
    }

    private void initIndicator(List<LimitBuyVo.ActivityTagsBean> list) {
        titleNavigator = new CommonNavigator(mActivity);
        titleNavigator.setAdjustMode(true);
        titleNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return list != null ? list.size() : 0;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int i) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                LimitBuyVo.ActivityTagsBean bean = list.get(i);
                simplePagerTitleView.setText(bean.activitytag);
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentHeightSize(28));
                simplePagerTitleView.setPadding(AutoUtils.getPercentWidthSize(20), 0, AutoUtils.getPercentWidthSize(20), 0);
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(mActivity, R.color.textMinor));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(mActivity, R.color.white));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(i);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setColors(ContextCompat.getColor(mActivity, R.color.white));

                WrapPagerIndicator indicator = new WrapPagerIndicator(context);
                indicator.setHorizontalPadding(AutoUtils.getPercentWidthSize(10));
                indicator.setFillColor(ContextCompat.getColor(mActivity, R.color.colorBrown));
                return indicator;
            }
        });

        mIndicator.setNavigator(titleNavigator);
        subtitleContainerHelper = new FragmentContainerHelper(mIndicator);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                subtitleContainerHelper.handlePageSelected(position);
            }
        });

        titleNavigator.onPageSelected(0);
        mViewPager.setCurrentItem(0, false);
    }
}
