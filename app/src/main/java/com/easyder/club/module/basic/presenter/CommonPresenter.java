package com.easyder.club.module.basic.presenter;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.easyder.club.module.common.AlertActivity;
import com.sky.wrapper.base.presenter.MvpBasePresenter;

/**
 * Auther:  winds
 * Data:    2018/4/25
 * Version: 1.0
 * Desc:
 */


public class CommonPresenter extends MvpBasePresenter {

    @Override
    protected void login() {
        Context context = getView() instanceof Context ? (Context) getView() : ((Fragment) getView()).getContext();
        context.startActivity(AlertActivity.getIntent(context));
    }

    @Override
    protected void setClientType() {
//        getData(ApiConfig.API_SET_CLIENT_TYPE, new RequestParams().put("type", "STORE_CASH_REGISTER").get(), BaseVo.class);
    }
}
