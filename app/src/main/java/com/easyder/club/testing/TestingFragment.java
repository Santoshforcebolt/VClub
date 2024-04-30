package com.easyder.club.testing;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.easyder.club.ApiConfig;
import com.easyder.club.BuildConfig;
import com.easyder.club.R;
import com.sky.widget.usage.ToastView;
import com.sky.wrapper.ManagerConfig;
import com.sky.wrapper.base.view.WrapperFragment;
import com.sky.wrapper.utils.PreferenceUtils;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author:  winds
 * Data:    2018/6/27
 * Version: 1.0
 * Desc:
 */


public class TestingFragment extends WrapperFragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_host)
    TextView et_host;
    @BindView(R.id.tv_mark)
    TextView tv_mark;

    TestingAdapter adapter;
    final String PRE_SELECTED = "testing_selected";
    final String PRE_INPUT = "testing_input";
    final String[] host = {"http://fbn.d.easyder.com/"};

    public static TestingFragment newInstance() {
        return new TestingFragment();
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_testing;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new TestingAdapter();
        mRecyclerView.setAdapter(adapter);
        adapter.setNewData(Arrays.asList(host));

        String host = PreferenceUtils.getPreference(_mActivity, PRE_INPUT, null);
        if (!TextUtils.isEmpty(host)) {
            et_host.setText(host);
        } else {
            adapter.setSelected(PreferenceUtils.getPreference(_mActivity, PRE_SELECTED, -1));
        }

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                PreferenceUtils.putPreference(_mActivity, PRE_SELECTED, position);
                adapter.setSelected(position);
                et_host.setText(null);
            }
        });
        tv_mark.setText(String.format("版本号:\b\b%1$s\n版本名称:\b\b%2$s\n打包时间:\b\b%3$s", BuildConfig.VERSION_CODE, BuildConfig.VERSION_NAME, BuildConfig.buildTime));
        et_host.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String toString = et_host.getText().toString();
                if (!TextUtils.isEmpty(toString)) {
                    adapter.setSelected(-1);
                }
            }
        });

    }

    @Override
    protected void loadData(Bundle savedInstanceState) {

    }

    @OnClick(R.id.btn_enter)
    public void click() {
        String host = et_host.getText().toString();
        if (!TextUtils.isEmpty(host)) {
            if (!host.startsWith("http")) {
                showToast("请输入全拼的站点地址");
                return;
            }
            ApiConfig.HOST = host;
            PreferenceUtils.putPreference(_mActivity, PRE_INPUT, host);
        } else {
            if (adapter.selected == -1) {
                ToastView.showToastInCenter(_mActivity, "请选择要进入的站点", Toast.LENGTH_SHORT);
                return;
            }
            ApiConfig.HOST = adapter.getItem(adapter.selected);

        }
        ManagerConfig.getInstance().setBaseHost(ApiConfig.HOST);
        enterNextPage(getActivity());
    }

    /**
     * 进入下一个页面
     *
     * @param activity
     */
    private void enterNextPage(Activity activity) {

    }


    public static class TestingAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public TestingAdapter() {
            super(R.layout.item_testing);
        }

        public int selected = -1;

        public void setSelected(int selected) {
            this.selected = selected;
            notifyDataSetChanged();
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            int i = mData.indexOf(item);
            helper.setVisible(R.id.iv_selected, selected == i);
            helper.setText(R.id.tv_no, String.valueOf(i + 1));
            helper.setText(R.id.tv_host, item);
        }
    }
}

