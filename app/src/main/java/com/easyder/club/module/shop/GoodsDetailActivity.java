package com.easyder.club.module.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.easyder.club.R;
import com.easyder.club.module.basic.event.TabIml;
import com.easyder.club.module.basic.event.ToggleChanged;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.common.MainActivity;
import com.gyf.immersionbar.ImmersionBar;
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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/11/25 17:45
 * Email: xcode126@126.com
 * Desc:
 */
public class GoodsDetailActivity extends WrapperSwipeActivity<CommonPresenter> {

    @BindView(R.id.rl_title_float)
    RelativeLayout rlTitleFloat;
    @BindView(R.id.rl_title_Indicator)
    RelativeLayout rlTitleIndicator;

    @BindView(R.id.mIndicator)
    MagicIndicator mIndicator;
    @BindView(R.id.mViewPager)
    SolveViewPager mSolveViewPager;

    TabAdapter adapter;
    private String[] titles;
    private String productcode;

    public static Intent getIntent(Context context, String productcode) {
        return new Intent(context, GoodsDetailActivity.class).putExtra("productcode", productcode);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_goods_detail;
    }

    @Override
    protected boolean isUseTitle() {
        return false;
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        titles = UIUtils.getStringArray(mActivity,R.array.a_goods_detail_title);
        ImmersionBar.with(mActivity).statusBarView(R.id.status_bar_view).statusBarDarkFont(true).init();
        productcode = intent.getStringExtra("productcode");
        showTitleType(true);
        initAdapter();
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {

    }

    /**
     * 标题栏是否悬浮
     *
     * @param isFloat
     */
    public void showTitleType(boolean isFloat) {
        rlTitleFloat.setVisibility(isFloat ? View.VISIBLE : View.GONE);
        rlTitleIndicator.setVisibility(isFloat ? View.GONE : View.VISIBLE);
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        adapter = new TabAdapter(getSupportFragmentManager(), initFragment());
        mSolveViewPager.setOffscreenPageLimit(titles.length);
        mSolveViewPager.setAdapter(adapter);
        mSolveViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                showTitleType(position == 0);
            }
        });
        initTitleIndicator(0);
    }

    /**
     * 初始化菜单
     *
     * @param index
     */
    private void initTitleIndicator(int index) {
        CommonNavigator titleNavigator = new CommonNavigator(mActivity);
//        titleNavigator.setAdjustMode(titles.length <= 4 ? true : false);//true 自适应居中、false 反之
        titleNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(titles[index]);
//                int width = titles.length <= 4 ? AutoUtils.getPercentWidthSize(5) : AutoUtils.getPercentWidthSize(20);
                int width= AutoUtils.getPercentWidthSize(40);
                simplePagerTitleView.setPadding(width, 0, width, 0);
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, AutoUtils.getPercentHeightSize(28));
                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.textMain));
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.colorRed));

                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPageIndex(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                linePagerIndicator.setColors(ContextCompat.getColor(mActivity, R.color.colorGreen));
                return null;
            }
        });
        titleNavigator.onPageSelected(index);
        mIndicator.setNavigator(titleNavigator);
        ViewPagerHelper.bind(mIndicator, mSolveViewPager);
    }

    /**
     * @return
     */
    private List<WrapperMvpFragment> initFragment() {
        List<WrapperMvpFragment> list = new ArrayList<>();
        list.add(GoodsDetailFragment.newInstance(productcode));
        list.add(GoodsEvaluateFragment.newInstance(productcode));
        return list;
    }

    /**
     * 切换界面
     *
     * @param index
     */
    public void showPageIndex(int index) {
        if (mSolveViewPager != null) {
            mSolveViewPager.setCurrentItem(index);
        }
    }

    @OnClick({R.id.iv_return, R.id.iv_return2, R.id.iv_home})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                finish();
                break;
            case R.id.iv_return2:
                if (mSolveViewPager.getCurrentItem() == 0) {
                    finish();
                } else {
                    showPageIndex(0);
                    showTitleType(true);
                }
                break;
            case R.id.iv_home:
                startActivity(MainActivity.getResetIntent(mActivity));
                EventBus.getDefault().post(new ToggleChanged(TabIml.TAB_HOME));
                break;
        }
    }

}
