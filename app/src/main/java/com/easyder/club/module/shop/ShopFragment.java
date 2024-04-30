package com.easyder.club.module.shop;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easyder.club.ApiConfig;
import com.easyder.club.AppConfig;
import com.easyder.club.R;
import com.easyder.club.App;
import com.easyder.club.module.basic.event.AccountChanged;
import com.easyder.club.module.basic.event.AccountIml;
import com.easyder.club.module.basic.event.OrderChanged;
import com.easyder.club.module.basic.event.OrderIml;
import com.easyder.club.module.basic.presenter.CommonPresenter;
import com.easyder.club.module.common.vo.CustomerBean;
import com.easyder.club.module.enjoy.PublishEnjoyActivity;
import com.easyder.club.module.enjoy.vo.GoodsListVo;
import com.easyder.club.module.me.AppointActivity;
import com.easyder.club.module.me.IntegralShopActivity;
import com.easyder.club.module.me.RechargeActivity;
import com.easyder.club.module.shop.adapter.ShopAdapter;
import com.easyder.club.module.shop.adapter.ShopMenuAdapter;
import com.easyder.club.module.shop.adapter.ShopTemplateFourAdapter;
import com.easyder.club.module.shop.adapter.ShopTemplateTwoAdapter;
import com.easyder.club.module.shop.vo.CarCountVo;
import com.easyder.club.module.shop.vo.ShopIndexVo;
import com.easyder.club.module.shop.vo.ShopRecommendVo;
import com.easyder.club.utils.DoubleUtil;
import com.easyder.club.utils.RequestParams;
import com.gyf.immersionbar.ImmersionBar;
import com.sky.widget.cluster.refresh.NestedRefreshLayout;
import com.sky.widget.component.HorizontalProgressBar;
import com.sky.wrapper.base.adapter.ViewHelper;
import com.sky.wrapper.base.listener.OnViewHelper;
import com.sky.wrapper.base.view.WrapperMvpFragment;
import com.sky.wrapper.core.manager.ImageManager;
import com.sky.wrapper.core.model.BaseVo;
import com.sky.wrapper.utils.CommonTools;

import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Author: sky on 2020/11/17 15:45
 * Email: xcode126@126.com
 * Desc: 商城首页
 */
public class ShopFragment extends WrapperMvpFragment<CommonPresenter>
        implements NestedRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mNestedRefreshLayout)
    NestedRefreshLayout mNestedRefreshLayout;
    @BindView(R.id.tv_count)
    TextView tvCount;
    private ShopAdapter shopAdapter;
    private int page, totalPage;

    public static ShopFragment newInstance() {
        return new ShopFragment();
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_shop;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        ImmersionBar.with(_mActivity).statusBarView(R.id.status_bar_view).statusBarColor("#2C3340").init();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initAdapter();
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
        onRefresh();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && presenter != null) {
            onRefresh();
        }
    }

    @Override
    public void onRefresh() {
        getShopIndex();
        getCarCount();
    }

    @Override
    public void onLoadMoreRequested() {
        if (page < totalPage) {
            getRecommendList(++page);
        } else {
            shopAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showContentView(String url, BaseVo dataVo) {
        if (url.contains(ApiConfig.API_SHOP_INDEX)) {
            processShopIndex((ShopIndexVo) dataVo);
        } else if (url.contains(ApiConfig.API_SHOP_RECOMMEND)) {
            processShopRecommend((ShopRecommendVo) dataVo);
        } else if (url.contains(ApiConfig.API_GET_CAR_TOTAL)) {
            processCarCount((CarCountVo) dataVo);
        }
        if (presenter.getRequestCount() <= 0) {
            mNestedRefreshLayout.refreshFinish();
        }
    }

    @Subscribe
    public void AccountChanged(AccountChanged changed) {
        switch (changed.sign) {
            case AccountIml.ACCOUNT_CAR_CHANGE:
                getCarCount();
                break;
        }
    }

    @Subscribe
    public void OrderChanged(OrderChanged changed) {
        switch (changed.sign) {
            case OrderIml.SIGN_ORDER_COMMIT_SUCCESS:
                getCarCount();
                break;
        }
    }

    @OnClick(R.id.fl_car)
    public void onViewClicked() {
        startActivity(ShopCarActivity.getIntent(_mActivity));
    }

    /**
     * get car count
     */
    private void getCarCount() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_GET_CAR_TOTAL, new RequestParams().putCid().get(), CarCountVo.class);
    }

    /**
     * get shop index
     */
    private void getShopIndex() {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_SHOP_INDEX, new RequestParams()
                .putCid()
                .get(), ShopIndexVo.class);
    }

    /**
     * get recommend list
     *
     * @param page
     */
    private void getRecommendList(int page) {
        presenter.setNeedDialog(false);
        presenter.postData(ApiConfig.API_SHOP_RECOMMEND, new RequestParams()
                .putCid()
//                .put("searchKey", searchKey)
                .put("page", page)
                .put("rows", AppConfig.PAGE_SIZE)
                .get(), ShopRecommendVo.class);
    }

    /**
     * process car count
     *
     * @param dataVo
     */
    private void processCarCount(CarCountVo dataVo) {
        if (dataVo != null) {
            tvCount.setText(String.valueOf(dataVo.total));
            tvCount.setVisibility(dataVo.total > 0 ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * process shop recommend
     *
     * @param dataVo
     */
    private void processShopRecommend(ShopRecommendVo dataVo) {
        if (page == 1) {
            totalPage = CommonTools.getTotalPage(dataVo.total, AppConfig.PAGE_SIZE);
            if (dataVo.total <= 0) {
                shopAdapter.getData().clear();
                shopAdapter.setEmptyView(getHelperView(mRecyclerView, R.layout.common_empty, null));
                shopAdapter.notifyDataSetChanged();
            } else {
                shopAdapter.setNewData(dataVo.rows);
            }
        } else {
            shopAdapter.addData(dataVo.rows);
            shopAdapter.loadMoreComplete();
        }
    }

    /**
     * process shop index
     *
     * @param dataVo
     */
    private void processShopIndex(ShopIndexVo dataVo) {
        getRecommendList(page = 1);
        //处理头部banner和menu部分
        if (shopAdapter.getHeaderLayoutCount() > 0) {
            shopAdapter.removeAllHeaderView();
            shopAdapter.notifyDataSetChanged();
        }
        shopAdapter.addHeaderView(getMenuHeaderView(dataVo.listAdverts, dataVo.listIndexMenus));

        //处理模板部分
        if (dataVo.timeLimitActivitie != null && dataVo.timeLimitActivitie.size() > 0) {
            for (int i = 0; i < dataVo.timeLimitActivitie.size(); i++) {
                ShopIndexVo.TimeLimitActivitieBean timeLimitActivitieBean = dataVo.timeLimitActivitie.get(i);
                ShopIndexVo.TimeLimitActivitieBean.TimeLimitActivityThemeTemplateBean template = timeLimitActivitieBean.timeLimitActivityThemeTemplate;
                int templatecode = template == null ? 0 : template.templatecode;
                shopAdapter.addHeaderView(getTemplateView(templatecode, timeLimitActivitieBean));
            }
        }
    }

    /**
     * 获取banner和menu视图
     *
     * @param listAdverts
     * @param menusBeans
     * @return
     */
    private View getMenuHeaderView(List<ShopIndexVo.ListAdvertsBean> listAdverts, List<ShopIndexVo.ListIndexMenusBean> menusBeans) {
        return getHelperView(mRecyclerView, R.layout.header_shop_menu, helper -> {
            BGABanner mBGABanner = helper.getView(R.id.mBGABanner);
            mBGABanner.setAdapter(new BGABanner.Adapter<ImageView, ShopIndexVo.ListAdvertsBean>() {
                @Override
                public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable ShopIndexVo.ListAdvertsBean item, int position) {

                    ImageManager.load(_mActivity, itemView, item.advertimg, R.drawable.ic_placeholder_2, R.drawable.ic_placeholder_2);
                }
            });
            mBGABanner.setDelegate((BGABanner.Delegate<ImageView, ShopIndexVo.ListAdvertsBean>) (banner, itemView, item, position) -> {
//                clickJump(item.adverturl, item.jumpcode > 0 ? jumpcode : -1);
                jumpBanner(item);
            });
            mBGABanner.setData(listAdverts, null);

            ShopMenuAdapter menuAdapter = new ShopMenuAdapter(menusBeans);
            RecyclerView mRecyclerView = helper.getView(R.id.mRecyclerView);
            mRecyclerView.setNestedScrollingEnabled(false);
            mRecyclerView.setLayoutManager(new GridLayoutManager(_mActivity, 5));
            mRecyclerView.setAdapter(menuAdapter);
            menuAdapter.setOnItemClickListener((adapter, view, position) -> {
                ShopIndexVo.ListIndexMenusBean item = menuAdapter.getItem(position);
                onClickMenu(item.url);
            });
            helper.setOnClickListener(R.id.ll_search, v -> startActivity(SearchActivity.getIntent(_mActivity)));
        });
    }

    /**
     * 根据编号获取对应模板视图
     *
     * @param code
     * @return
     */
    private View getTemplateView(int code, ShopIndexVo.TimeLimitActivitieBean bean) {
        if (code == 1) {
            return getTemplateOneView(bean);
        } else if (code == 2) {
            return getTemplateTwoView(bean);
        } else if (code == 3) {
            return getTemplateThreeView(bean);
        } else if (code == 4) {
            return getTemplateFourView(bean);
        }
        return getTemplateOneView(bean);
    }

    /**
     * 获取模板一视图
     *
     * @param bean
     * @return
     */
    private View getTemplateOneView(ShopIndexVo.TimeLimitActivitieBean bean) {
        return getHelperView(mRecyclerView, R.layout.header_shop_template_one, helper -> {
            //处理标题和背景
            String title;
            if (bean.timeLimitActivityThemeTemplate != null) {
                ShopIndexVo.TimeLimitActivitieBean.TimeLimitActivityThemeTemplateBean template = bean.timeLimitActivityThemeTemplate;
                title = TextUtils.isEmpty(template.title) ? getString(R.string.a_member_special_price) : template.title;
                helper.setImageManager(_mActivity, R.id.iv_bg, template.bgurl, R.drawable.background, R.drawable.background);
            } else {
                title = getString(R.string.a_member_special_price);
            }
            helper.setText(R.id.tv_title, title);

            //处理商品
            LinearLayout layoutGoods = helper.getView(R.id.layout_goods);
            layoutGoods.removeAllViews();
            if (bean.timeLimitActivityDetaileds != null && bean.timeLimitActivityDetaileds.size() > 0) {
                int count = (bean.timeLimitActivityDetaileds.size() <= 3) ? bean.timeLimitActivityDetaileds.size() : 3;
                for (int i = 0; i < count; i++) {
                    ShopIndexVo.TimeLimitActivitieBean.TimeLimitActivityDetailedsBean goods = bean.timeLimitActivityDetaileds.get(i);
                    layoutGoods.addView(getTemplateOneItemView(layoutGoods, goods));
                }
            }

            //处理点击
            helper.setOnClickListener(R.id.ll_look_more, v -> clickJump(AppConfig.LIMIT_BUY, bean.activitycode));
        });
    }

    /**
     * 获取模板二视图
     *
     * @param bean
     * @return
     */
    private View getTemplateTwoView(ShopIndexVo.TimeLimitActivitieBean bean) {
        return getHelperView(mRecyclerView, R.layout.header_shop_template_two, new OnViewHelper() {
            @Override
            public void help(ViewHelper helper) {
                //处理标题和背景
                String title;
                if (bean.timeLimitActivityThemeTemplate != null) {
                    ShopIndexVo.TimeLimitActivitieBean.TimeLimitActivityThemeTemplateBean template = bean.timeLimitActivityThemeTemplate;
                    title = TextUtils.isEmpty(template.title) ? getString(R.string.a_limit_sec_kill) : template.title;
//                    helper.setImageManager(_mActivity, R.id.iv_bg, template.bgurl, R.drawable.background, R.drawable.background);
                } else {
                    title = getString(R.string.a_limit_sec_kill);
                }
                helper.setText(R.id.tv_title, title);

                //处理商品
                RecyclerView mRecyclerView = helper.getView(R.id.mRecyclerView);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false));
                ShopTemplateTwoAdapter twoAdapter = new ShopTemplateTwoAdapter(bean.timeLimitActivityDetaileds);
                mRecyclerView.setAdapter(twoAdapter);

                //处理点击
                twoAdapter.setOnItemChildClickListener((adapter, view, position) -> clickJump(AppConfig.LIMIT_BUY, bean.activitycode));
                twoAdapter.setOnItemClickListener((adapter, view, position) -> {
                    ShopIndexVo.TimeLimitActivitieBean.TimeLimitActivityDetailedsBean item = twoAdapter.getItem(position);
                    startActivity(GoodsDetailActivity.getIntent(_mActivity, item.itemcode));
                });
            }
        });
    }

    /**
     * 获取模板三视图
     *
     * @param bean
     * @return
     */
    private View getTemplateThreeView(ShopIndexVo.TimeLimitActivitieBean bean) {
        return getHelperView(mRecyclerView, R.layout.header_shop_template_three, helper -> {
            //处理标题和背景
            String title;
            String subTitle;
            if (bean.timeLimitActivityThemeTemplate != null) {
                ShopIndexVo.TimeLimitActivitieBean.TimeLimitActivityThemeTemplateBean template = bean.timeLimitActivityThemeTemplate;
                title = TextUtils.isEmpty(template.title) ? getString(R.string.a_limit_sec_kill) : template.title;
                subTitle = TextUtils.isEmpty(template.minititle) ? getString(R.string.a_surprised_tip) : template.title;
                helper.setImageManager(_mActivity, R.id.iv_bg, template.bgurl, R.drawable.background, R.drawable.background);
            } else {
                title = getString(R.string.a_limit_sec_kill);
                subTitle = getString(R.string.a_surprised_tip);
            }
            helper.setText(R.id.tv_title, title);
            helper.setText(R.id.tv_sub_title, subTitle);

            //处理商品
            LinearLayout layoutGoods = helper.getView(R.id.layout_goods);
            layoutGoods.removeAllViews();
            if (bean.timeLimitActivityDetaileds != null && bean.timeLimitActivityDetaileds.size() > 0) {
                int count = (bean.timeLimitActivityDetaileds.size() <= 3) ? bean.timeLimitActivityDetaileds.size() : 3;
                for (int i = 0; i < count; i++) {
                    ShopIndexVo.TimeLimitActivitieBean.TimeLimitActivityDetailedsBean goods = bean.timeLimitActivityDetaileds.get(i);
                    layoutGoods.addView(getTemplateThreeItemView(layoutGoods, goods));
                }
            }

            //处理点击
            helper.setOnClickListener(R.id.tv_look_more, v -> clickJump(AppConfig.LIMIT_BUY, bean.activitycode));
        });
    }

    /**
     * 获取模板四视图
     *
     * @param bean
     * @return
     */
    private View getTemplateFourView(ShopIndexVo.TimeLimitActivitieBean bean) {
        return getHelperView(mRecyclerView, R.layout.header_shop_template_four, new OnViewHelper() {
            @Override
            public void help(ViewHelper helper) {
                //处理标题和背景
                String title;
                if (bean.timeLimitActivityThemeTemplate != null) {
                    ShopIndexVo.TimeLimitActivitieBean.TimeLimitActivityThemeTemplateBean template = bean.timeLimitActivityThemeTemplate;
                    title = TextUtils.isEmpty(template.title) ? getString(R.string.a_festival_activity) : template.title;
                    helper.setImageManager(_mActivity, R.id.iv_bg, template.bgurl, R.drawable.background, R.drawable.background);
                } else {
                    title = getString(R.string.a_festival_activity);
                }
                helper.setText(R.id.tv_title, title);

                //处理商品
                RecyclerView mRecyclerView = helper.getView(R.id.mRecyclerView);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, false));
                ShopTemplateFourAdapter twoAdapter = new ShopTemplateFourAdapter(bean.timeLimitActivityDetaileds);
                mRecyclerView.setAdapter(twoAdapter);

                //处理点击
                twoAdapter.setOnItemChildClickListener((adapter, view, position) -> clickJump(AppConfig.LIMIT_BUY, bean.activitycode));
                twoAdapter.setOnItemClickListener((adapter, view, position) -> {
                    ShopIndexVo.TimeLimitActivitieBean.TimeLimitActivityDetailedsBean item = twoAdapter.getItem(position);
                    startActivity(GoodsDetailActivity.getIntent(_mActivity, item.itemcode));
                });
            }
        });
    }

    /**
     * 获取模板一对应的商品item
     *
     * @param layout
     * @return
     */
    private View getTemplateOneItemView(LinearLayout layout, ShopIndexVo.TimeLimitActivitieBean.TimeLimitActivityDetailedsBean bean) {
        return getHelperView(layout, R.layout.item_shop_template_one, helper -> {
            helper.setImageManager(_mActivity, R.id.iv_image, bean.imgurl, R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
            helper.setText(R.id.tv_name, bean.itemname);
            helper.setText(R.id.tv_price, String.format("%1$s%2$.2f", "£", bean.price));
            helper.setTextDelete(R.id.tv_original_price, String.format("%1$s%2$.2f", "£", bean.marketprice));
            helper.setVisible(R.id.tv_original_price, bean.marketprice <= bean.price);
            helper.setOnClickListener(R.id.layout_container, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(GoodsDetailActivity.getIntent(_mActivity, bean.itemcode));
                }
            });
        });
    }

    /**
     * 获取模板三对应的商品item
     *
     * @param layout
     * @return
     */
    private View getTemplateThreeItemView(LinearLayout layout, ShopIndexVo.TimeLimitActivitieBean.TimeLimitActivityDetailedsBean bean) {
        return getHelperView(layout, R.layout.item_shop_template_three, new OnViewHelper() {
            @Override
            public void help(ViewHelper helper) {
                helper.setImageManager(_mActivity, R.id.iv_image, bean.imgurl, R.drawable.ic_placeholder_1, R.drawable.ic_placeholder_1);
                helper.setText(R.id.tv_name, bean.itemname);
                helper.setText(R.id.tv_price, String.format("%1$s%2$.2f", "£", bean.price));
                helper.setTextDelete(R.id.tv_original_price, String.format("%1$s%2$.2f", "£", bean.marketprice));
                helper.setVisible(R.id.tv_original_price, bean.marketprice <= bean.price);
                helper.setText(R.id.tv_last, String.format("%1$s%2$s%3$s", getString(R.string.a_last), bean.stocknum, getString(R.string.a_unit_jian)));
                HorizontalProgressBar mHorizontalProgressBar = helper.getView(R.id.mHorizontalProgressBar);
                if (bean.totalnum == 0 || bean.totalnum - bean.stocknum <= 0) {
                    mHorizontalProgressBar.setProgress(100);
                    helper.setText(R.id.tv_progress, "100%");
//                    helper.setVisible(R.id.ll_progress_bar, false);
                } else {
//                    helper.setVisible(R.id.ll_progress_bar, true);
                    int progress = (int) Math.rint(DoubleUtil.round((bean.totalnum - (bean.stocknum == -1 ? 0 : bean.stocknum)) / bean.totalnum * 100, 2, BigDecimal.ROUND_HALF_UP));
                    mHorizontalProgressBar.setProgress(progress);
                    helper.setText(R.id.tv_progress, String.format("%1$s%2$s", progress, "%"));
                }

                helper.setOnClickListener(R.id.layout_container, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(GoodsDetailActivity.getIntent(_mActivity, bean.itemcode));
                    }
                });
            }
        });
    }

    /**
     * 菜单栏 点击事件
     *
     * @param url
     */
    public void onClickMenu(String url) {
        clickJump(url, -1);
    }

    /**
     * 点击菜单栏的 跳转事件
     *
     * @param url
     * @param jumpcode 当=-1 时就代表是 点击菜单栏跳转事件 >0时代表banner跳转事件
     */
    private void clickJump(String url, int jumpcode) {
        switch (url) {
            case AppConfig.RECEIVE_TICKET_CENTER://领券中心
                startActivity(GetTicketCenterActivity.getIntent(_mActivity));
                break;
            case AppConfig.LIMIT_BUY://限时抢购
                startActivity(LimitBuyActivity.getIntent(_mActivity, jumpcode > 0 ? jumpcode : -1));
                break;
            case AppConfig.PRODUCT_LIST://产品
            case AppConfig.PROJECT_LIST://项目
                startActivity(GoodsListActivity.getIntent(_mActivity));
                break;
            case AppConfig.INTEGRAL_SHOP://积分兑换
                CustomerBean customer = App.getCustomer();
                if (customer != null) {
                    startActivity(IntegralShopActivity.getIntent(_mActivity, String.valueOf(customer.score)));
                }
                break;
            case AppConfig.ALL_MEAL://全部套餐
                startActivity(MealActivity.getIntent(_mActivity));
                break;
            case AppConfig.APPOINT://预约
                startActivity(AppointActivity.getIntent(_mActivity));
                break;
        }
    }

    /**
     * 广告跳转类型(
     * produce：产品；
     * item：项目；
     * groups：拼团；
     * flashSale：限时抢购；
     * combo：套餐 ；
     * topup：充值 ；
     * appointment：预约 ；
     * score：积分商城 ；
     * none：无跳转 )
     *
     * @param item
     */
    private void jumpBanner(ShopIndexVo.ListAdvertsBean item) {
        switch (item.jumptype) {
            case "produce"://产品
                startActivity(GoodsDetailActivity.getIntent(_mActivity, item.jumpid));
                break;
            case "item"://项目
                startActivity(GoodsDetailActivity.getIntent(_mActivity, item.jumpid));
                break;
            case "groups"://拼团
                break;
            case "flashSale"://限时抢购
                startActivity(LimitBuyActivity.getIntent(_mActivity, Integer.parseInt(item.jumpid)));
                break;
            case "combo"://套餐
                startActivity(MealActivity.getIntent(_mActivity));
                break;
            case "topup"://充值
                startActivity(RechargeActivity.getIntent(_mActivity));
                break;
            case "appointment"://预约
                startActivity(AppointActivity.getIntent(_mActivity));
                break;
            case "score"://积分商城
                CustomerBean customer = App.getCustomer();
                if (customer != null) {
                    startActivity(IntegralShopActivity.getIntent(_mActivity, String.valueOf(customer.score)));
                }
                break;
        }
    }

    /**
     * 初始化Adapter
     */
    private void initAdapter() {
        mNestedRefreshLayout.setOnRefreshListener(this);
        shopAdapter = new ShopAdapter();
        shopAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        mRecyclerView.setAdapter(shopAdapter);
        shopAdapter.setHeaderAndEmpty(true);
        shopAdapter.setOnItemClickListener((adapter, view, position) -> {
            ShopRecommendVo.RowsBean item = shopAdapter.getItem(position);
            startActivity(GoodsDetailActivity.getIntent(_mActivity, item.piccode));
        });
        shopAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ShopRecommendVo.RowsBean item = shopAdapter.getItem(position);
                GoodsListVo.ProductsBean bean = new GoodsListVo.ProductsBean(item.piccode, item.picname, item.imgurl);
                startActivity(PublishEnjoyActivity.getIntent(_mActivity, bean));
            }
        });
    }
}
