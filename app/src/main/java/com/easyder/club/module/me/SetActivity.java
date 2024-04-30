package com.easyder.club.module.me;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.easyder.club.ApiConfig;
import com.easyder.club.BuildConfig;
import com.easyder.club.R;
import com.easyder.club.App;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.common.ConfirmDialog;
import com.easyder.club.module.common.vo.VersionVo;
import com.easyder.club.utils.RequestParams;
import com.easyder.club.utils.UpdateUtils;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperSwipeActivity;
import com.sky.wrapper.core.model.BaseVo;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/11/18 11:26
 * Email: xcode126@126.com
 * Desc:
 */
public class SetActivity extends WrapperSwipeActivity<CommonPresenter> {

    @BindView(R.id.tv_version)
    TextView tvVersion;

    public static Intent getIntent(Context context) {
        return new Intent(context, SetActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_set;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        tvVersion.setText(BuildConfig.VERSION_NAME);
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {

    }

    @OnClick({R.id.fl_about, R.id.fl_update, R.id.tv_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_about:
                break;
            case R.id.fl_update:
                checkVersion();
                break;
            case R.id.tv_logout:
                requestLogOut();
                break;
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_LOGOUT)) {
            processLogOutSuccess(dataVo);
        } else if (url.contains(ApiConfig.API_APP_CHECK_VERSION)) {
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
     * 请求退出
     */
    private void requestLogOut() {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_LOGOUT, new RequestParams().putCid().get(), BaseVo.class);
    }

    /**
     * process check version
     *
     * @param versionVo
     */
    private void processCheckVersion(VersionVo versionVo) {
        if (TextUtils.equals(versionVo.isexistupdate,"yes")) {
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
                    }
                }, R.id.btn_confirm);
            }).show();
        }else {
            showToast(getString(R.string.a_yet_is_new_version));
        }
    }

    /**
     * 处理退出成功
     *
     * @param dataVo
     */
    private void processLogOutSuccess(BaseVo dataVo) {
        App.setCustomerBean(null);
        showToast(getString(R.string.a_logout_success));
//        PreferenceUtils.putPreference(mActivity, Helper.BASIC_ACCOUNT, ""); //历史账号
//        EventBus.getDefault().post(new ToggleChanged(TabIml.TAB_HOME));
        this.finish(); //主动退出登陆    关掉当前页面
        startActivity(LoginActivity.getIntent(mActivity));
//        this.overridePendingTransition(R.anim.slide_bottom_in, 0);
    }
}
