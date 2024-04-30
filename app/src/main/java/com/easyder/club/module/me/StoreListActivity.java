package com.easyder.club.module.me;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.me.adapter.StoreListAdapter;
import com.easyder.club.module.me.vo.StoreListVo;
import com.easyder.club.utils.RequestParams;
import com.gyf.immersionbar.ImmersionBar;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperStatusActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/11/17 16:25
 * Email: xcode126@126.com
 * Desc: 门店列表
 */
public class StoreListActivity extends WrapperStatusActivity<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener {

    @BindView(R.id.status_bar_view)
    View statusBarView;
    @BindView(R.id.et_keyword)
    EditText etKeyword;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private StoreListAdapter listAdapter;
    private String keyword, tel;

    public static Intent getIntent(Context context) {
        return new Intent(context, StoreListActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_store_list;
    }

    @Override
    protected boolean isUseTitle() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        super.initView(savedInstanceState, titleView, intent);
        ImmersionBar.with(mActivity).statusBarView(R.id.status_bar_view).statusBarDarkFont(true).init();
        initAdapter();
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getStoreList();
    }

    @OnClick({R.id.iv_left, R.id.tv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.tv_search:
                keyword = etKeyword.getText().toString().trim();
                mNestedRefreshLayout.froceRefreshToState(true);
                break;
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        super.showContentView(url, dataVo);
        if (url.contains(ApiConfig.API_CAN_APPOINT_STORE)) {
            processStore((StoreListVo) dataVo);
        }
    }

    /**
     * get store
     */
    private void getStoreList() {
        presenter.setNeedDialog(false);
        presenter.getData(ApiConfig.API_CAN_APPOINT_STORE, new RequestParams()
                .putWithoutEmpty("shopname", keyword)
                .putCid()
                .get(), StoreListVo.class);
    }

    /**
     * @param dataVo
     */
    private void processStore(StoreListVo dataVo) {
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
     * init adapter
     */
    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        listAdapter = new StoreListAdapter();
        mRecyclerView.setAdapter(listAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
        listAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                StoreListVo.RowsBean item = listAdapter.getItem(position);
                listAdapter.setSelected(position);
                Intent intent = new Intent();
                intent.putExtra("deptname", item.deptname);
                intent.putExtra("deptcode", item.deptcode);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        listAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                StoreListVo.RowsBean item = listAdapter.getItem(position);
                tel = item.tel;
                call(tel);
            }
        });
    }

    public static final int REQUEST_CALL_PERMISSION = 10111; //拨号请求码

    /**
     * 判断是否有某项权限
     *
     * @param string_permission 权限
     * @param request_code      请求码
     * @return
     */
    public boolean checkReadPermission(String string_permission, int request_code) {
        boolean flag = false;
        if (ContextCompat.checkSelfPermission(this, string_permission) == PackageManager.PERMISSION_GRANTED) {//已有权限
            flag = true;
        } else {//申请权限
            ActivityCompat.requestPermissions(this, new String[]{string_permission}, request_code);
        }
        return flag;
    }

    /**
     * 检查权限后的回调
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL_PERMISSION: //拨打电话
                if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {//失败
                    showToast(getString(R.string.a_call_permission));
                } else {//成功
                    call(tel);
                }
                break;
        }
    }

    /**
     * 拨打电话（直接拨打）
     *
     * @param telPhone 电话
     */
    public void call(String telPhone) {
        if (checkReadPermission(Manifest.permission.CALL_PHONE, REQUEST_CALL_PERMISSION)) {
            CommonTools.callPhone(this, telPhone);
        }
    }

}
