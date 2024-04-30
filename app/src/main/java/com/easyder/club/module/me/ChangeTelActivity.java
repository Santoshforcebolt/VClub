package com.easyder.club.module.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.easyder.club.R;
import com.easyder.club.App;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperSwipeActivity;
import com.sky.wrapper.core.model.BaseVo;

/**
 * Author：sky on 2019/5/30 16:37.
 * Email：xcode126@126.com
 * Desc：修改手机号
 */

public class ChangeTelActivity extends WrapperSwipeActivity<CommonPresenter> {

    public static Intent getIntent(Context mContext, String tel) {
        return new Intent(mContext, ChangeTelActivity.class).putExtra("tel", tel);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        titleView.setTitle(getString(R.string.a_change_tel));
        loadVerifyPage(intent.getStringExtra("tel"));
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {

    }

    /**
     * 加载旧手机验证界面
     *
     * @param tel
     */
    private void loadVerifyPage(String tel) {
        loadRootFragment(R.id.container, VerifyTelFragment.newInstance(tel), false, false);
    }

    /**
     * 加载新手机修改页面
     */
    public void loadChangePage(String oldTel,String oldCode) {
        loadRootFragment(R.id.container, ChangeTelFragment.newInstance(oldTel, oldCode), false, false);
    }

    /**
     * 处理手机更改成功
     */
    public void changeTelSuccess(){
        App.setCustomerBean(null);
        startActivity(LoginActivity.getIntent(mActivity));
        finish();
    }
}
