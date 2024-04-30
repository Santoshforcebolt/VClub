package com.easyder.club.module.common;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.easyder.club.ApiConfig;
import com.easyder.club.BuildConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.event.ToggleChanged;
import com.easyder.club.module.collect.CollectFragment;
import com.easyder.club.module.common.vo.VersionVo;
import com.easyder.club.module.enjoy.EnjoyFragment;
import com.easyder.club.module.home.HomeFragment;
import com.easyder.club.module.me.MeFragment;
import com.easyder.club.module.shop.ShopFragment;
import com.easyder.club.utils.RequestParams;
import com.easyder.club.utils.UpdateUtils;
import com.easyder.club.widget.NormalTabItem;
import com.sky.widget.cluster.tabbar.TabBar;
import com.sky.widget.component.ScrollableViewPager;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.adapter.TabAdapter;
import com.sky.wrapper.base.presenter.MvpBasePresenter;
import com.sky.wrapper.base.view.WrapperMvpFragment;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends WrapperStatusActivity<MvpBasePresenter> {

    @BindView(R.id.mViewPager)
    ScrollableViewPager mViewPager;
    @BindView(R.id.mTabBar)
    TabBar mTabBar;

    private TabAdapter adapter;

    public static Intent getIntent(Context mContext) {
        return new Intent(mContext, MainActivity.class);
    }

    public static Intent getResetIntent(Context context) {
        return new Intent(context, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean isUseTitle() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        initTab();
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        checkVersion();
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_APP_CHECK_VERSION)) {
            processCheckVersion((VersionVo) dataVo);
        }
    }

    /**
     * check version
     */
    private void checkVersion() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_APP_CHECK_VERSION, new RequestParams()
                .put("device", "android")
                .put("currentverion", BuildConfig.VERSION_CODE)
                .putCid()
                .get(), VersionVo.class);
    }

    /**
     * process check version
     *
     * @param versionVo
     */
    private void processCheckVersion(VersionVo versionVo) {
        if (TextUtils.equals(versionVo.isexistupdate, "yes")) {
            new ConfirmDialog(mActivity).setHelperCallback((dialog, helper) -> {
                helper.setText(R.id.tv_title, getString(R.string.a_version_update));
                helper.setText(R.id.tv_content, versionVo.remark);
                helper.setOnClickListener(v -> {
                    switch (v.getId()) {
                        case R.id.btn_confirm:
                            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                //申请权限
                                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        0x10);
                                showToast(getString(R.string.a_allow_download_install));
                            } else {
                                UpdateUtils.downloadApp(mActivity, versionVo.updateurl, versionVo.currentverion + "");
                            }
                            dialog.dismiss();
                            break;
                        case R.id.btn_cancel:
                        case R.id.iv_close:
                            if (!TextUtils.equals(versionVo.isforceupdate, "yes")) {
                                dialog.dismiss();
                            }
                            break;
                    }
                }, R.id.btn_confirm, R.id.btn_cancel);
            }).show();
        }
    }

    /**
     * 初始化tab
     */
    private void initTab() {
        List<WrapperMvpFragment> list = new ArrayList<>();
        list.add(HomeFragment.newInstance());
        list.add(ShopFragment.newInstance());
        list.add(CollectFragment.newInstance());
        list.add(EnjoyFragment.newInstance());
        list.add(MeFragment.newInstance());

        adapter = new TabAdapter(getSupportFragmentManager(), list);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(list.size());
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

            }
        });

        mTabBar.addItem(new NormalTabItem(mActivity).init(R.drawable.selector_tab_home, getString(R.string.a_home)));
        mTabBar.addItem(new NormalTabItem(mActivity).init(R.drawable.selector_tab_business, getString(R.string.a_business)));
        mTabBar.addItem(new NormalTabItem(mActivity).init(R.drawable.selector_tab_collect, getString(R.string.a_collect)));
        mTabBar.addItem(new NormalTabItem(mActivity).init(R.drawable.selector_tab_enjoy, getString(R.string.a_enjoy)));
        mTabBar.addItem(new NormalTabItem(mActivity).init(R.drawable.selector_tab_member, getString(R.string.a_my)));

        mViewPager.setCurrentItem(0);
        mTabBar.setCurrentItem(0);
        mTabBar.setOnTabSelectedListener(new TabBar.OnTabSelectedListener() {
            @Override
            public boolean beforeTabSelected(int position, int prePosition) {
                return false;
            }

            @Override
            public void onTabSelected(int position, int prePosition) {
                if (mViewPager != null && mViewPager.getAdapter() != null) {
                    onTabClick(position, prePosition);
                }
            }

            @Override
            public void onTabUnSelected(int position) {
            }

            @Override
            public void onTabReSelected(int position) {
            }
        });
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            exitDialog();
//            return true;
//        }
//        return false;
//    }

    /**
     * exit dialog
     */
    private void exitDialog() {
//        new ConfirmDialog(mActivity).setHelperCallback(new WrapperDialog.HelperCallback() {
//            @Override
//            public void feedback(Dialog dialog, ViewHelper helper) {
//                helper.setText(R.id.tv_content, getString(R.string.a_is_exit_app));
//                helper.setOnClickListener(v -> {
//                    switch (v.getId()) {
//                        case R.id.btn_confirm:
//                            BluetoothHelp.getInstance().disconnect();
//                            dialog.dismiss();
//                            finish();
//                            break;
//                    }
//                }, R.id.btn_confirm);
//            }
//        }).show();
    }

    @Subscribe
    public void toggleChanged(ToggleChanged changed) {
        onTabClick(changed.tab, 0);
    }

    /**
     * tab被選中
     *
     * @param position
     * @param prePosition
     */
    private void onTabClick(int position, int prePosition) {
        mViewPager.setCurrentItem(position);
        mTabBar.setCurrentItem(position);
//        mViewPager.post(new Runnable() {
//            @Override
//            public void run() {
//                mViewPager.setCurrentItem(position);
//            }
//        });
//        switch (position) {
//            case 0:
//                mViewPager.setCurrentItem(0, false);
//                break;
//            case 1:
//                mViewPager.setCurrentItem(1, false);
//                break;
//            case 2:
//                mViewPager.setCurrentItem(2, false);
//                break;
//
//        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                exitTime = System.currentTimeMillis();
                showToast(getString(R.string.a_again_press_exit_app));
            } else {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}


