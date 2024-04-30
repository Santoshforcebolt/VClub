package com.easyder.club.module.me;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.ApiConfig;
import com.easyder.club.AppConfig;
import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.me.adapter.MyTicketAdapter;
import com.easyder.club.module.me.vo.MyTicketVo;
import com.easyder.club.utils.RequestParams;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.wrapper.base.view.WrapperMvpFragment;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import butterknife.BindView;

/**
 * Author: sky on 2020/11/24 14:51
 * Email: xcode126@126.com
 * Desc:
 */
public class MyTicketFragment extends WrapperMvpFragment<CommonPresenter> implements
        NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;

    private MyTicketAdapter ticketAdapter;

    public static final int TYPE_NOT_USE = 1;//可使用
    public static final int TYPE_YET_USE = 2;//已使用
    public static final int TYPE_PAST_DUE = 3;//已过期

    private int page, totalPage, type;

    public static MyTicketFragment newInstance(int type) {
        MyTicketFragment fragment = new MyTicketFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getViewLayout() {
        return R.layout.common_refresh;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        type = getArguments().getInt("type");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        ticketAdapter = new MyTicketAdapter(type);
        mRecyclerView.setAdapter(ticketAdapter);
        mNestedRefreshLayout.setOnRefreshListener(this);
        ticketAdapter.setOnLoadMoreListener(this, mRecyclerView);
        ticketAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MyTicketVo.RowsBean item = ticketAdapter.getItem(position);
//                new Share(_mActivity).shareSmallProgram(String.format(AppConfig.SMALL_PROGRAM_SEND_COUPON, WrapperApplication.getMemberVo().recommencode,item.instancecode,item.customercode),AppConfig.SMALL_PROGRAM_COUPON_COVER,getString(R.string.a_send_you_a_coupon));
            }
        });
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
        mNestedRefreshLayout.froceRefreshToState(true);
    }

    @Override
    public void onRefresh() {
        getTicketData(page = 1);

    }

    @Override
    public void onLoadMoreRequested() {
        if (page < totalPage) {
            getTicketData(++page);
        } else {
            ticketAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_MY_TICKET)) {
            processTicketData((MyTicketVo) dataVo);
        }
    }

    /**
     * 获取优惠券数据
     *
     * @param page
     */
    private void getTicketData(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_MY_TICKET, new RequestParams()
                .put("state", type)
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .putCid()
                .get(), MyTicketVo.class);
    }

    /**
     * 处理优惠券信息
     *
     * @param dataVo
     */
    private void processTicketData(MyTicketVo dataVo) {
        if (dataVo == null) {
            dataVo = new MyTicketVo();
        }
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total <= 0) {
                ticketAdapter.getData().clear();
                ticketAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                ticketAdapter.notifyDataSetChanged();
            } else {
                ticketAdapter.setNewData(dataVo.rows);
            }
            mNestedRefreshLayout.refreshFinish();
        } else {
            ticketAdapter.addData(dataVo.rows);
            ticketAdapter.loadMoreComplete();
        }
    }

//    private UMShareListener shareListener = new UMShareListener() {
//
//        @Override
//        public void onStart(SHARE_MEDIA share_media) {
//
//        }
//
//        @Override
//        public void onResult(SHARE_MEDIA share_media) {
//            showToast("分享成功");
//            onLazyInitView(null);
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//            showToast("分享失败" + throwable.getMessage());
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA share_media) {
//            showToast("取消分享");
//        }
//    };
}
