package com.easyder.club.module.me;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.BaseRecyclerHolder;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.me.vo.AddressListVo;
import com.easyder.club.module.me.vo.RegionVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.view.WrapperDialog;
import com.sky.wrapper.base.view.WrapperSwipeActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/11/20 10:55
 * Email: xcode126@126.com
 * Desc:
 */
public class NewAddressActivity extends WrapperSwipeActivity<CommonPresenter> {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_tel)
    EditText etTel;
    @BindView(R.id.tv_province)
    TextView tvProvince;
    @BindView(R.id.et_address)
    EditText etAddress;

    private AddressListVo.RowsBean rowsBean;
//    private StringBuilder sbAddress, sbAddressCode;
    private int id, defaultflag;

    /**
     * 新增地址，不用传递数据
     *
     * @param mContext
     * @return
     */
    public static Intent getIntent(Context mContext) {
        return new Intent(mContext, NewAddressActivity.class);
    }

    /**
     * 修改地址，需要传递数据
     *
     * @param mContext
     * @param rowsBean
     * @return
     */
    public static Intent getIntent(Context mContext, AddressListVo.RowsBean rowsBean) {
        return new Intent(mContext, NewAddressActivity.class).putExtra("rowsBean", rowsBean);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_new;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        titleView.setTitle(getString(R.string.a_add_edit_address));

        rowsBean = (AddressListVo.RowsBean) intent.getSerializableExtra("rowsBean");
        handleData(rowsBean);
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_ADD_ADDRESS) || url.contains(ApiConfig.API_CHANGE_ADDRESS)) {
            processAddAddress(dataVo);
        } else if (url.contains(ApiConfig.API_GET_PROVINCE_CITY)) {
            processGetData((RegionVo) dataVo);
        }
    }

    /**
     * 获取省数据
     */
    private void getProvinceCity() {
        getProvinceCity(-1);
    }

    /**
     * 获取省市地区
     *
     * @param id
     */
    private void getProvinceCity(int id) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_GET_PROVINCE_CITY, new RequestParams()
                .putWithoutEmpty("id", id)
                .putCid()
                .get(), RegionVo.class);
    }
    /**
     * 修改地址
     *
     * @param addresscode
     * @param addressname
     * @param detailedaddre
     * @param receivername
     * @param receivertel
     */
    private void changeAddress(String addresscode, String addressname, String detailedaddre,
                               String receivername, String receivertel, String mId) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_CHANGE_ADDRESS, new RequestParams()
//                .put("addresscode", addresscode)
//                .put("addressname", addressname)
                .put("detailedaddre", detailedaddre)
                .put("defaultflag", defaultflag)
                .put("receivername", receivername)
                .put("receivertel", receivertel)
                .put("id", mId)
//                .put("addflag",addflag)
//                .put("zipcode",zipcode)
                .putCid()
                .get(), BaseVo.class);
    }

    /**
     * @param addresscode
     * @param addressname
     * @param detailedaddre
     * @param receivername
     * @param receivertel
     */
    private void addAddress(String addresscode, String addressname, String detailedaddre,
                            String receivername, String receivertel) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_ADD_ADDRESS, new RequestParams()
//                .put("addresscode", addresscode)
//                .put("addressname", addressname)
                .put("detailedaddre", detailedaddre)
                .put("defaultflag", defaultflag = 1)//0:否；1：是；
                .put("receivername", receivername)
                .put("receivertel", receivertel)
//                .put("addflag",addflag)
                .putCid()//18770854980
                .get(), BaseVo.class);
    }

    /**
     * 处理获取的省市区数据
     *
     * @param dataVo
     */
    private void processGetData(RegionVo dataVo) {
        showSelectDialog(dataVo);
    }

    /**
     * 处理数据显示
     *
     * @param rowsBean
     */
    private void handleData(AddressListVo.RowsBean rowsBean) {
        if (rowsBean != null) {
            etName.setText(rowsBean.receivername);
            etTel.setText(rowsBean.receivertel);
//            tvProvince.setText(rowsBean.addressname);
            etAddress.setText(rowsBean.detailedaddre);
//            sbAddress = new StringBuilder(rowsBean.addressname);
//            sbAddressCode = new StringBuilder(rowsBean.addresscode);
            defaultflag = rowsBean.defaultflag;
        }
    }

    /**
     * 处理添加地址成功
     *
     * @param dataVo
     */
    private void processAddAddress(BaseVo dataVo) {
        setResult(RESULT_OK);
        this.finish();
    }

    /**
     * 选择省市区窗口
     */
    private void showSelectDialog(final RegionVo regionVo) {
        new SelectRegionDialog(mActivity).setHelperCallback(new WrapperDialog.HelperCallback() {
            @Override
            public void help(final Dialog dialog, final ViewHelper helper) {
//                helper.setText(R.id.tv_address, sbAddress.toString());
                RecyclerView mRecyclerView = helper.getView(R.id.mRecyclerView);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
                final SelectAdapter selectAdapter = new SelectAdapter(regionVo.rows);
                DividerItemDecoration divider = new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL);
                divider.setDrawable(UIUtils.getDrawable(R.drawable.shape_divider_line));
                mRecyclerView.addItemDecoration(divider);
                mRecyclerView.setAdapter(selectAdapter);
                selectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        RegionVo.RowsBean item = selectAdapter.getItem(position);
                        selectRegion(item, helper, dialog);
                    }
                });
            }

            private void selectRegion(RegionVo.RowsBean item, ViewHelper helper, Dialog dialog) {
                id = item.id;
//                sbAddress.append(item.name);
//                sbAddressCode.append(item.id);
//                helper.setText(R.id.tv_address, sbAddress.toString());

                if (item.type >= 3) {
//                    tvProvince.setText(sbAddress.toString());
                } else {
//                    sbAddressCode.append(",");
//                    sbAddress.append(",");
                    getProvinceCity(id);
                }
                dialog.dismiss();
            }
        }).show();
    }

    private class SelectAdapter extends BaseQuickAdapter<RegionVo.RowsBean, BaseRecyclerHolder> {

        public SelectAdapter(@Nullable List<RegionVo.RowsBean> data) {
            super(R.layout.item_select_address, data);
        }
        @Override
        protected void convert(BaseRecyclerHolder helper, RegionVo.RowsBean item) {
            helper.setText(R.id.tv_address, item.name);
        }

    }
    @OnClick({R.id.btn_save, R.id.tv_province})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                saveAddress();
                break;
            case R.id.tv_province:
//                sbAddress = new StringBuilder();
//                sbAddressCode = new StringBuilder();
                getProvinceCity();
                break;

        }
    }

    /**
     * 保存地址信息
     */
    private void saveAddress() {
        String receivername = etName.getText().toString().trim();
        if (TextUtils.isEmpty(receivername)) {
            showToast("请填写收件人姓名");
            return;
        }
        String receivertel = etTel.getText().toString().trim();
        if (TextUtils.isEmpty(receivertel)) {
            showToast("请填写收件人手机");
            return;
        }
//        String addressname = tvProvince.getText().toString().trim();
//        if (TextUtils.isEmpty(addressname)) {
//            showToast("请选择省市区地址");
//            return;
//        }
        String detailedaddre = etAddress.getText().toString().trim();
        if (TextUtils.isEmpty(detailedaddre)) {
            showToast("请填写详细地址");
            return;
        }
        if (rowsBean == null) {
            addAddress("sbAddressCode.toString()"," sbAddress.toString()", detailedaddre, receivername, receivertel);
        } else {
            changeAddress("sbAddressCode.toString()", "sbAddress.toString()", detailedaddre, receivername, receivertel, rowsBean.id);
        }
    }
}
