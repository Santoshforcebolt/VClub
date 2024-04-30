package com.easyder.club.module.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.me.adapter.AddressListAdapter;
import com.easyder.club.module.me.vo.AddressListVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/11/20 10:34
 * Email: xcode126@126.com
 * Desc: 收货地址
 */
public class AddressListActivity extends WrapperStatusActivity<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener{

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private AddressListAdapter listAdapter;
    public static final String TRANSPORT_ADDRESS_KEY = "address";
    private boolean isSelect;

    /**
     * @param mContext
     * @param isSelect 是否是选择地址
     * @return
     */
    public static Intent getIntent(Context mContext, boolean isSelect) {
        return new Intent(mContext, AddressListActivity.class).putExtra("isSelect", isSelect);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_address_list;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        titleView.setTitle(getString(R.string.a_receive_address));
        isSelect = intent.getBooleanExtra("isSelect", false);
        initAdapter();
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getAddressData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //添加或者修改收货地址成功
            onRefresh();
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_ADDRESS_LIST)) {
            processAddressList((AddressListVo) dataVo);
        }else if (url.contains(ApiConfig.API_SET_DEFAULT_ADDRESS)) {
            onRefresh();
        }else if (url.contains(ApiConfig.API_DELETE_ADDRESS)) {
            onRefresh();
        }
    }

    /**
     * 获取收货地址列表数据信息
     */
    private void getAddressData() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_ADDRESS_LIST, new RequestParams()
                .putCid()
                .get(), AddressListVo.class);
    }

    /**
     * 设置默认收货地址
     *
     * @param id
     */
    private void setDefaultAddress(String id) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_SET_DEFAULT_ADDRESS, new RequestParams()
                .put("id", id)
                .putCid()
                .get(), BaseVo.class);
    }

    /**
     * 删除收货地址
     *
     * @param id
     */
    private void deleteAddress(String id) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_DELETE_ADDRESS, new RequestParams()
                .put("id", id)
                .putCid()
                .get(), BaseVo.class);
    }

    /**
     * 处理地址列表信息
     *
     * @param dataVo
     */
    private void processAddressList(AddressListVo dataVo) {
        if (dataVo.rows != null && dataVo.rows.size() > 0) {
            listAdapter.setNewData(dataVo.rows);
        } else {
            listAdapter.getData().clear();
            listAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
            listAdapter.notifyDataSetChanged();
        }
        mNestedRefreshLayout.refreshFinish();
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        listAdapter = new AddressListAdapter(isSelect);
        mRecyclerView.setAdapter(listAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
        listAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                AddressListVo.RowsBean item = listAdapter.getItem(position);
                switch (view.getId()) {
                    case R.id.dtv_select:
                        setDefaultAddress(item.id);
                        break;
                    case R.id.iv_edit:
                        enChangeAddress(item);
                        break;
                    case R.id.iv_delete:
                        deleteAddress(item.id);
                        break;
                }

            }
        });
        listAdapter.setOnItemClickListener((adapter, view, position) -> {
            AddressListVo.RowsBean item = listAdapter.getItem(position);
            onReturnData(item);
        });
    }
    /**
     * 处理回传数据
     *
     * @param item
     */
    private void onReturnData(AddressListVo.RowsBean item) {
        if (isSelect) {
            Intent intent = new Intent();
            intent.putExtra("id", item.id);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
    @OnClick({R.id.ll_add_address})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ll_add_address:
                enChangeAddress(null);
                break;
        }
    }

    /**
     * 进入新增或者修改地址界面
     *
     * @param rowsBean 需要被编辑的数据
     */
    private void enChangeAddress(AddressListVo.RowsBean rowsBean) {
        if (rowsBean == null) {
            startActivityForResult(NewAddressActivity.getIntent(mActivity), 0x00);
        } else {
            startActivityForResult(NewAddressActivity.getIntent(mActivity, rowsBean), 0x00);
        }
    }

}
