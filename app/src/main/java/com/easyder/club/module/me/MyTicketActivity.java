package com.easyder.club.module.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.sky.widget.autolayout.utils.AutoUtils;
import com.sky.widget.component.SolveViewPager;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.adapter.TabAdapter;
import com.sky.wrapper.base.view.WrapperMvpFragment;
import com.sky.wrapper.base.view.WrapperSwipeActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.UIUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Author: sky on 2020/11/24 14:50
 * Email: xcode126@126.com
 * Desc:
 */
public class MyTicketActivity extends WrapperSwipeActivity<CommonPresenter> {

    @BindView(R.id.mIndicator)
    MagicIndicator mIndicator;
    @BindView(R.id.mViewPager)
    SolveViewPager mViewPager;

    private String[] titles;
    private TabAdapter adapter;

    public static Intent getIntent(Context mContext) {
        return new Intent(mContext, MyTicketActivity.class);
    }
    @Override
    protected int getViewLayout() {
        return R.layout.common_indicator;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        initAdapter(titleView, intent);
        initTitleIndicator();
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {

    }

    /**
     * 初始化界面
     *
     * @return
     */
    private List<WrapperMvpFragment> initProjectTicketList() {
        List<WrapperMvpFragment> list = new ArrayList<>();
        list.add(MyTicketFragment.newInstance(MyTicketFragment.TYPE_NOT_USE));
        list.add(MyTicketFragment.newInstance(MyTicketFragment.TYPE_YET_USE));
        list.add(MyTicketFragment.newInstance(MyTicketFragment.TYPE_PAST_DUE));
        return list;
    }

    /**
     * 初始化Adapter
     *
     * @param titleView
     * @param intent
     */
    private void initAdapter(TitleView titleView, Intent intent) {
        titles = UIUtils.getStringArray(mActivity,R.array.a_ticket_array);
        titleView.setTitle(getString(R.string.a_ticket));
        adapter = new TabAdapter(getSupportFragmentManager(), initProjectTicketList());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(titles.length);
    }

    /**
     * 初始化菜单栏
     */
    private void initTitleIndicator() {
        CommonNavigator titleNavigator = new CommonNavigator(mActivity);
        titleNavigator.setAdjustMode(true);//true 自适应居中、false 反之
        titleNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(titles[index]);
                simplePagerTitleView.setPadding(0, 0, 0, 0);
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentHeightSize(28));
                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.textLesser));
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.textSuper));
                simplePagerTitleView.setText(titles[index]);

                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setColors(ContextCompat.getColor(mActivity, R.color.white));
                return linePagerIndicator;
            }
        });
        mIndicator.setNavigator(titleNavigator);
        ViewPagerHelper.bind(mIndicator, mViewPager);
    }

}
