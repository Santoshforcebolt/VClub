package com.easyder.club.module.common;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.easyder.club.R;
import com.easyder.club.App;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.me.LoginActivity;
import com.gyf.immersionbar.ImmersionBar;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperMvpActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.LanguageUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;

/**
 * Author: sky on 2020/6/8 17:46.
 * Email:xcode126@126.com
 * Desc:
 */
public class SplashActivity extends WrapperMvpActivity<CommonPresenter> {

    @BindView(R.id.iv_image)
    ImageView ivImage;

    /**
     * 权限组
     */
    private static final String[] permissionsGroup = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.VIBRATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected int getViewLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void setImmersionBar() {
        ImmersionBar.with(mActivity).fullScreen(true).init();
    }

    @Override
    protected boolean isUseTitle() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
//        Glide.with(mActivity).load(R.drawable.splash_bg).apply(RequestOptions.bitmapTransform(
//                new GlideScaleBottomTransform(AutoUtils.getScreenHeight() / AutoUtils.getScreenWidth())))
//                .into(ivImage);

    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        LanguageUtils.applyLanguage(mActivity, LanguageUtils.getCurrentLanguage(mActivity));
        requestPermission();
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {

    }

    /**
     * 获得权限
     *
     * @param gain
     */
    private void onPermissionGain(boolean gain) {
        new Handler().postDelayed(() -> intoNextPage(), 2000);
    }

    /**
     * 进入下页
     */
    private void intoNextPage() {
        if (App.isLogin()) {
            startActivity(MainActivity.getIntent(mActivity));
        } else {
            startActivity(LoginActivity.getIntent(mActivity));
        }
        this.finish();
    }

    /**
     * 请求权限
     */
    private void requestPermission() {
        new RxPermissions(this).request(permissionsGroup)
                .subscribe(new io.reactivex.Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {//授权成功
                            onPermissionGain(false);
                        } else {
                            showToast("权限被拒绝");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
//                .subscribe(new Observer<Boolean>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//
//                    @Override
//                    public void onNext(Boolean aBoolean) {
//                        if (!aBoolean) {
////                            showToast("权限被拒绝");
//                        }
//                        onPermissionGain(false);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        showToast("申请权限出错");
//                        onPermissionGain(false);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                    }
//                });
    }


}
