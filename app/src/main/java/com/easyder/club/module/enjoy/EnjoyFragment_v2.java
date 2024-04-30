package com.easyder.club.module.enjoy;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.ApiConfig;
import com.easyder.club.AppConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.event.AccountChanged;
import com.easyder.club.module.basic.event.AccountIml;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.enjoy.adapter.EnjoyAdapter;
import com.easyder.club.module.enjoy.vo.EnjoyListVo;
import com.easyder.club.utils.RequestParams;
import com.gyf.immersionbar.ImmersionBar;
import com.sky.widget.autolayout.utils.AutoUtils;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.view.WrapperDialog;
import com.sky.wrapper.base.view.WrapperMvpFragment;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Author: sky on 2020/11/13 14:49
 * Email: xcode126@126.com
 * Desc: 评鉴:使用一个界面
 */
@Deprecated
public class EnjoyFragment_v2 extends WrapperMvpFragment<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.tv_my_enjoy)
    TextView tvMyEnjoy;
    @BindView(R.id.tv_my_love)
    TextView tvMyLove;
    @BindView(R.id.tv_my_wish)
    TextView tvMyWish;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private EnjoyAdapter enjoyAdapter;
    private int page, totalPage;
    private static final String TYPE_TOTAL = "";
    private static final String TYPE_WISH = "wish";
    private static final String TYPE_COLLECT = "collection";

    public static EnjoyFragment_v2 newInstance() {
        return new EnjoyFragment_v2();
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_enjoy;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBar.with(_mActivity).statusBarView(R.id.status_bar_view).statusBarColor(R.color.colorFore).init();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        enjoyAdapter = new EnjoyAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRecyclerView.setAdapter(enjoyAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
        enjoyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                EnjoyListVo.RowsBean item = enjoyAdapter.getItem(position);
                startActivityForResult(EnjoyDetailActivity.getIntent(_mActivity, item.id), 0x01);
            }
        });
        enjoyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                EnjoyListVo.RowsBean item = enjoyAdapter.getItem(position);
                switch (view.getId()) {
                    case R.id.ll_collect:
                        int c = item.collection == 0 ? 1 : 0;
                        changeCollect(item.id, TYPE_COLLECT, c);
                        break;
                    case R.id.ll_wish:
                        int w = item.wish == 0 ? 1 : 0;
                        changeCollect(item.id, TYPE_WISH, w);
                        break;
                }
            }
        });
        setAllSelected(tvMyEnjoy);
    }

    @Subscribe
    public void AccountChanged(AccountChanged changed) {
        switch (changed.sign) {
            case AccountIml.ACCOUNT_ENJOY_CHANGE:
                onRefresh();
                break;
        }
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        getEnjoyList(page = 1);
    }

    @Override
    public void onLoadMoreRequested() {
        if (page < totalPage) {
            getEnjoyList(++page);
        } else {
            enjoyAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_ENJOY_LIST)) {
            processEnjoyList((EnjoyListVo) dataVo);
        } else if (url.contains(ApiConfig.API_CHANGE_WISH)) {
            onRefresh();
        }
    }

    /**
     * @param evalid
     * @param optiontype
     * @param optionvalue
     */
    private void changeCollect(int evalid, String optiontype, int optionvalue) {
        presenter.setNeedDialog(true);
        presenter.postData(ApiConfig.API_CHANGE_WISH, new RequestParams()
                .putCid()
                .put("evalid", evalid)
                .put("optiontype", optiontype)
                .put("optionvalue", optionvalue)
                .get(), BaseVo.class);
    }

    /**
     * get enjoy list
     *
     * @param page
     */
    private void getEnjoyList(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_ENJOY_LIST, new RequestParams()
                .putCid()
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .putWithoutEmpty("optiontype", getOptionType())
                .get(), EnjoyListVo.class);
    }

    /**
     * @param dataVo
     */
    private void processEnjoyList(EnjoyListVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total <= 0) {
                enjoyAdapter.getData().clear();
                enjoyAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                enjoyAdapter.notifyDataSetChanged();
            } else {
                enjoyAdapter.setNewData(dataVo.rows);
            }
            mNestedRefreshLayout.refreshFinish();
            tvMyEnjoy.setText(String.format("%1$s(%2$s)", getString(R.string.a_my_enjoy), dataVo.sumtotal));
            tvMyLove.setText(String.format("%1$s(%2$s)", getString(R.string.a_my_favorite), dataVo.collection));
            tvMyWish.setText(String.format("%1$s(%2$s)", getString(R.string.a_wish_list), dataVo.wish));
        } else {
            enjoyAdapter.addData(dataVo.rows);
            enjoyAdapter.loadMoreComplete();
        }
    }

    @OnClick({R.id.ll_edit, R.id.tv_my_enjoy, R.id.tv_my_love, R.id.tv_my_wish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_edit:
                publishDialog();
                break;
            case R.id.tv_my_enjoy:
            case R.id.tv_my_love:
            case R.id.tv_my_wish:
                setAllSelected(view);
                onRefresh();
                break;
        }
    }

    /**
     *
     */
    private void publishDialog() {
        new WrapperDialog(_mActivity) {
            @Override
            public int getLayoutRes() {
                return R.layout.dialog_publish_enjoy;
            }

            @Override
            protected void setDialogParams(Dialog dialog) {
                setDialogParams(dialog, WindowManager.LayoutParams.MATCH_PARENT, AutoUtils.getPercentHeightSize(630), Gravity.BOTTOM);
            }

            @Override
            public void help(ViewHelper helper) {
                helper.setOnClickListener(v -> {
                    switch (v.getId()) {
                        case R.id.ll_create:
                            startActivity(PublishEnjoyActivity.getIntent(_mActivity));
                            break;
                        case R.id.ll_find:
                            startActivity(EnjoyListActivity.getIntent(_mActivity));
                            break;
                    }
                    dismiss();
                }, R.id.ll_find, R.id.ll_create, R.id.btn_cancel);
            }
        }.show();
    }

    /**
     * get option type
     *
     * @return
     */
    private String getOptionType() {
        if (tvMyLove.isSelected()) {
            return TYPE_COLLECT;
        } else if (tvMyWish.isSelected()) {
            return TYPE_WISH;
        } else {
            return TYPE_TOTAL;
        }
    }

    /**
     * set all select
     *
     * @param view
     */
    private void setAllSelected(View view) {
        tvMyEnjoy.setSelected(view == tvMyEnjoy);
        tvMyLove.setSelected(view == tvMyLove);
        tvMyWish.setSelected(view == tvMyWish);
    }

    /**
     * get total
     *
     * @param listVo
     * @return
     */
    private int getTotal(EnjoyListVo listVo) {
        String optionType = getOptionType();
        if (TextUtils.equals(optionType, TYPE_COLLECT)) {
            return listVo.collection;
        }
        if (TextUtils.equals(optionType, TYPE_WISH)) {
            return listVo.wish;
        }
        return listVo.total;
    }

}
