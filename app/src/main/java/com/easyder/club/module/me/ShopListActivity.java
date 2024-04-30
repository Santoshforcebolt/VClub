package com.easyder.club.module.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.me.adapter.ShopListAdapter;
import com.easyder.club.module.me.vo.PartnerDetailVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;

import butterknife.BindView;

/**
 * Author：sky on 2019/7/1 11:38.
 * Email：xcode126@126.com
 * Desc：门店列表
 */

public class ShopListActivity extends WrapperStatusActivity<CommonPresenter> implements NestedRefreshLayout.OnRefreshListener {

    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private ShopListAdapter adapter;
    private String partnercode;

    public static Intent getIntent(Context mContext, String partnercode) {
        return new Intent(mContext, ShopListActivity.class).putExtra("partnercode", partnercode);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.common_refresh;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        titleView.setTitle(getString(R.string.a_shop_list));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        adapter = new ShopListAdapter();
        mRecyclerView.setAdapter(adapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        partnercode = intent.getStringExtra("partnercode");
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getGiftPartner(partnercode);
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_PARTNER_ADDRESS_LIST)) {
            processStoreList((PartnerDetailVo) dataVo);
        }
    }

    /**
     * 处理数据
     *
     * @param dataVo
     */
    private void processStoreList(PartnerDetailVo dataVo) {
        mNestedRefreshLayout.refreshFinish();
        if (dataVo.datas != null && dataVo.datas.size() > 0) {
            adapter.setNewData(dataVo.datas);
        } else {
            adapter.getData().clear();
            adapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取商家信息
     *
     * @param parentcode
     */
    private void getGiftPartner(String parentcode) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_PARTNER_ADDRESS_LIST, new RequestParams()
                .put("partnercode", parentcode)
                .putCid()
                .get(), PartnerDetailVo.class);
    }
}
