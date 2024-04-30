package com.easyder.club.module.shop;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.shop.adapter.BrandAdapter;
import com.easyder.club.module.shop.adapter.KindAdapter;
import com.easyder.club.module.shop.vo.BrandVo;
import com.easyder.club.module.shop.vo.KindVo;
import com.easyder.club.utils.RequestParams;
import com.sky.wrapper.base.view.WrapperDialogFragment;
import com.sky.wrapper.core.model.BaseVo;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/12/7 14:20
 * Email: xcode126@126.com
 * Desc:
 */
public class OptionFragment extends WrapperDialogFragment<CommonPresenter> {

    @BindView(R.id.et_min_price)
    EditText etMinPrice;
    @BindView(R.id.et_max_price)
    EditText etMaxPrice;
    @BindView(R.id.et_keyword)
    EditText etKeyword;
    @BindView(R.id.kindRecyclerView)
    RecyclerView kindRecyclerView;
    @BindView(R.id.brandRecyclerView)
    RecyclerView brandRecyclerView;

    @BindView(R.id.ll_kind)
    LinearLayout llKind;
    @BindView(R.id.ll_brand)
    LinearLayout llBrand;

    private KindAdapter kindAdapter;
    private BrandAdapter brandAdapter;
    private KindVo kindVo;
    private BrandVo brandVo;

    public static OptionFragment newInstance() {
        return new OptionFragment();
    }

    @Override
    public int getViewLayout() {
        return R.layout.dialog_option_goods;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setWindowAnimations(R.style.TransDialogAnim);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        kindRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        kindAdapter = new KindAdapter();
        kindRecyclerView.setAdapter(kindAdapter);
        kindRecyclerView.setNestedScrollingEnabled(false);
        kindAdapter.setOnItemClickListener((adapter, view, position) -> kindAdapter.setSelected(position));

        brandRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        brandAdapter = new BrandAdapter();
        brandRecyclerView.setNestedScrollingEnabled(false);
        brandRecyclerView.setAdapter(brandAdapter);
        brandAdapter.setOnItemClickListener((adapter, view, position) -> brandAdapter.setSelected(position));
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
        getKind();
        getBrand();
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_GET_KIND)) {
            processKind((KindVo) dataVo);
        } else if (url.contains(ApiConfig.API_GET_BRAND)) {
            processBrand((BrandVo) dataVo);
        }
    }

    /**
     * @param dataVo
     */
    private void processBrand(BrandVo dataVo) {
        this.brandVo = dataVo;
        if (dataVo.rows.size() > 0) {
            if (dataVo.rows.size() >= 8) {
                brandAdapter.setNewData(dataVo.rows.subList(0, 8));
            } else {
                brandAdapter.setNewData(dataVo.rows);
            }
        }
    }

    /**
     * @param dataVo
     */
    private void processKind(KindVo dataVo) {
        this.kindVo = dataVo;
        if (dataVo.total > 0) {
            if (dataVo.total >= 8) {
                kindAdapter.setNewData(dataVo.data.subList(0, 8));
            } else {
                kindAdapter.setNewData(dataVo.data);
            }
        }
    }

    /**
     * get kind
     */
    private void getKind() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_GET_KIND, new RequestParams()
                .putCid()
                .put("groupType", "product")
                .get(), KindVo.class);
    }

    /**
     * get brand
     */
    private void getBrand() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_GET_BRAND, new RequestParams()
                .putCid()
                .get(), BrandVo.class);
    }

    @OnClick({R.id.btn_confirm, R.id.ll_kind, R.id.ll_brand})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                int minPrice = 0;
                int maxPrice = 0;
                String min = etMinPrice.getText().toString().trim();
                String max = etMaxPrice.getText().toString().trim();
                if (!TextUtils.isEmpty(max)) {
                    maxPrice = Integer.parseInt(max);
                }
                if (!TextUtils.isEmpty(min)) {
                    minPrice = Integer.parseInt(min);

                }
                String keyword = etKeyword.getText().toString().trim();
                if (optionResultCallback != null) {
                    optionResultCallback.optionResult(minPrice, maxPrice, keyword, kindAdapter.getGroupCode(), brandAdapter.getBrandCode());
                }
                dismissAllowingStateLoss();
                break;
            case R.id.ll_kind:
                llKind.setSelected(!llKind.isSelected());
                if (llKind.isSelected()) {
                    kindAdapter.setNewData(kindVo.data);
                } else {
                    kindAdapter.setNewData(kindVo.data.subList(0, kindVo.data.size() >= 8 ? 8 : kindVo.data.size()));
                }
                break;
            case R.id.ll_brand:
                llBrand.setSelected(!llBrand.isSelected());
                if (llBrand.isSelected()) {
                    brandAdapter.setNewData(brandVo.rows);
                } else {
                    brandAdapter.setNewData(brandVo.rows.subList(0, kindVo.data.size() >= 8 ? 8 : kindVo.data.size()));
                }
                break;
        }
    }

    public void setOptionResultCallback(OptionResultCallback optionResultCallback) {
        this.optionResultCallback = optionResultCallback;
    }

    private OptionResultCallback optionResultCallback;

    public interface OptionResultCallback {
        void optionResult(int tempMinPrice, int tempMaxPrice, String temKeyword, int tempGroupCode, int tempBrandCode);
    }
}
