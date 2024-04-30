package com.easyder.club.module.shop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easyder.club.Helper;
import com.easyder.club.R;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.gyf.immersionbar.ImmersionBar;
import com.sky.widget.autolayout.utils.AutoUtils;
import com.sky.widget.usage.TitleView;
import com.sky.wrapper.base.view.WrapperSwipeActivity;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.PreferenceUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: sky on 2020/11/20 11:03
 * Email: xcode126@126.com
 * Desc:
 */
public class SearchActivity extends WrapperSwipeActivity<CommonPresenter> {

    @BindView(R.id.et_keyword)
    EditText etKeyword;
    @BindView(R.id.mTagFlowLayout)
    TagFlowLayout mTagFlowLayout;

    private List<String> searchList = new ArrayList<>();
    private String keyword;
    private String search;

    public static Intent getIntent(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected boolean isUseTitle() {
        return false;
    }

    @Override
    protected void initView(Bundle savedInstanceState, TitleView titleView, Intent intent) {
        ImmersionBar.with(mActivity).statusBarView(R.id.status_bar_view).statusBarColor(R.color.colorFore).init();
        initSearchHistory();
        etKeyword.setImeActionLabel(getString(R.string.a_search), EditorInfo.IME_ACTION_SEARCH);
        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (event.getAction() == KeyEvent.ACTION_UP) {
                        onSearch();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {

    }

    @OnClick({R.id.iv_left, R.id.tv_search, R.id.iv_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.tv_search:
                onSearch();
                break;
            case R.id.iv_clear:
                PreferenceUtils.putPreference(mActivity, Helper.BASIC_SEARCH, ""); //历史搜索记录
                initSearchHistory();
                break;
        }
    }

    /**
     * 初始化搜索历史
     */
    private void initSearchHistory() {
        searchList.clear();
        search = PreferenceUtils.getPreference(mActivity, Helper.BASIC_SEARCH, null);
        if (!TextUtils.isEmpty(search)) {
            searchList.addAll(Arrays.asList(search.split(",")));
        }
        setFlowLayoutData(mTagFlowLayout, searchList);
    }

    /**
     * 是否历史记录里有当前输入的关键字
     *
     * @param keyWords
     * @return
     */
    private boolean isThereKeyWord(String keyWords) {
        for (int i = 0; i < searchList.size(); i++) {
            if (searchList.get(i).equals(keyWords)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 进行搜索
     */
    private void onSearch() {
        keyword = etKeyword.getText().toString().trim();
        String searchs;
        if (!TextUtils.isEmpty(keyword)) {
            if (!isThereKeyWord(keyword)) {
                if (TextUtils.isEmpty(search)) {
                    searchs = String.format("%1$s,", keyword);
                } else {
                    searchs = String.format("%1$s%2$s,", search, keyword);
                }
                PreferenceUtils.putPreference(mActivity, Helper.BASIC_SEARCH, searchs); //历史搜索记录
                initSearchHistory();
            }
            startActivity(GoodsListActivity.getIntent(mActivity,keyword));
        } else {
            showToast(getString(R.string.a_input_keyword));
        }

    }

    /**
     * 流式布局
     *
     * @param mFlowLayout
     * @param shopitemshow
     */
    private void setFlowLayoutData(TagFlowLayout mFlowLayout, List<String> shopitemshow) {
        TagAdapter tagAdapter = new TagAdapter<String>(shopitemshow) {
            @Override
            public View getView(FlowLayout parent, int position, final String s) {
                LinearLayout.LayoutParams layoutParamss = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParamss.setMargins(0, 20, 20, 0);
                View view = View.inflate(mActivity, R.layout.item_search_history, null);
                TextView tv = view.findViewById(R.id.tv_search_history);
                AutoUtils.autoTextSize(tv, 22);
                AutoUtils.auto(view);
                tv.setText(s);
                view.setLayoutParams(layoutParamss);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        keyword = s;
                        startActivity(GoodsListActivity.getIntent(mActivity,keyword));
                    }
                });
                return tv;
            }
        };
        mFlowLayout.setAdapter(tagAdapter);
        mFlowLayout.getAdapter().notifyDataChanged();
    }
}
