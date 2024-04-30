package com.easyder.club.module.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;


/**
 * Author：sky on 2019/9/6 18:10.
 * Email：xcode126@126.com
 * Desc：
 */

public class TestActivity extends WrapperStatusActivity<CommonPresenter> {

    public static Intent getIntent(Context mContext){
        return new Intent(mContext,TestActivity.class);
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_testing;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
//        super.initView(savedInstanceState, titleView, intent);
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
//        super.showContentView(url, dataVo);
    }
}
