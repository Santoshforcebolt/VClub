package com.easyder.club.module.shop;

import android.os.Bundle;

import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CustomerPresenter;
import com.sky.wrapper.base.view.WrapperMvpFragment;
import com.sky.wrapper.core.model.BaseVo;

/**
 * Author: sky on 2020/11/20 17:51
 * Email: xcode126@126.com
 * Desc: 筛选
 */
public class FiltrateGoodsFragment extends WrapperMvpFragment<CustomerPresenter> {

    public FiltrateGoodsFragment newInstance() {
        FiltrateGoodsFragment fragment = new FiltrateGoodsFragment();
        return fragment;
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_filtrate_goods;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected void loadData(Bundle savedInstanceState) {

    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {

    }
}
