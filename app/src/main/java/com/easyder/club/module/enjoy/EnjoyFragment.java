package com.easyder.club.module.enjoy;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.easyder.club.ApiConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.event.AccountChanged;
import com.easyder.club.module.basic.event.AccountIml;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.enjoy.vo.EnjoyListVo;
import com.easyder.club.utils.RequestParams;
import com.easyder.club.utils.SimplePageChangeListener;
import com.gyf.immersionbar.ImmersionBar;
import com.sky.widget.autolayout.utils.AutoUtils;
import com.sky.widget.component.SolveViewPager;
import com.sky.wrapper.base.adapter.TabAdapter;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.view.WrapperDialog;
import com.sky.wrapper.base.view.WrapperMvpFragment;
import com.sky.wrapper.core.model.BaseVo;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/11/13 14:49
 * Email: xcode126@126.com
 * Desc: 评鉴
 */
public class EnjoyFragment extends WrapperMvpFragment<CommonPresenter> {

    @BindView(R.id.mSolveViewPager)
    SolveViewPager mSolveViewPager;
    @BindView(R.id.tv_my_enjoy)
    TextView tvMyEnjoy;
    @BindView(R.id.tv_my_love)
    TextView tvMyLove;
    @BindView(R.id.tv_my_wish)
    TextView tvMyWish;

    private TabAdapter adapter;

    public static EnjoyFragment newInstance() {
        return new EnjoyFragment();
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && presenter != null) {
            getEnjoyList();
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setPageSelected(0);
        initAdapter();
    }

    @Subscribe
    public void AccountChanged(AccountChanged changed) {
        switch (changed.sign) {
            case AccountIml.ACCOUNT_ENJOY_CHANGE:
                getEnjoyList();
                break;
            case AccountIml.ACCOUNT_PUBLISH_ENJOY:
                setPageSelected(0);
                getEnjoyList();
                break;

        }
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
        getEnjoyList();
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_ENJOY_LIST)) {
            processEnjoyList((EnjoyListVo) dataVo);
        } else if (url.contains(ApiConfig.API_CHANGE_WISH)) {
            getEnjoyList();
        }
    }

    /**
     * get enjoy list
     */
    private void getEnjoyList() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_ENJOY_LIST, new RequestParams()
                .putCid()
                .put("page", 1)
                .put("rows", 1)
//                .putWithoutEmpty("optiontype", getOptionType())
                .get(), EnjoyListVo.class);
    }

    /**
     * @param dataVo
     */
    private void processEnjoyList(EnjoyListVo dataVo) {
        tvMyEnjoy.setText(String.format("%1$s(%2$s)", getString(R.string.a_my_enjoy), dataVo.sumtotal));
        tvMyLove.setText(String.format("%1$s(%2$s)", getString(R.string.a_my_favorite), dataVo.collection));
        tvMyWish.setText(String.format("%1$s(%2$s)", getString(R.string.a_wish_list), dataVo.wish));
    }

    @OnClick({R.id.ll_edit, R.id.tv_my_enjoy, R.id.tv_my_love, R.id.tv_my_wish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_edit:
                publishDialog();
                break;
            case R.id.tv_my_enjoy:
                setPageSelected(0);
                break;
            case R.id.tv_my_love:
                setPageSelected(1);
                break;
            case R.id.tv_my_wish:
                setPageSelected(2);
                break;
        }
    }

    /**
     * publish dialog
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
     * set all select
     *
     * @param position
     */
    private void setPageSelected(int position) {
        tvMyEnjoy.setSelected(position == 0);
        tvMyLove.setSelected(position == 1);
        tvMyWish.setSelected(position == 2);
        mSolveViewPager.setCurrentItem(position);
    }

    /**
     * 初始化Adapter
     */
    private void initAdapter() {
        adapter = new TabAdapter(getChildFragmentManager(), initFragment());
        mSolveViewPager.setAdapter(adapter);
        mSolveViewPager.setOffscreenPageLimit(3);
        mSolveViewPager.addOnPageChangeListener(new SimplePageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setPageSelected(position);
            }
        });
    }

    /**
     * init fragment
     *
     * @return
     */
    private List<WrapperMvpFragment> initFragment() {
        List<WrapperMvpFragment> list = new ArrayList<>();
        list.add(EnjoyChildFragment.newInstance(""));
        list.add(EnjoyChildFragment.newInstance("collection"));
        list.add(EnjoyChildFragment.newInstance("wish"));
        return list;
    }
}
