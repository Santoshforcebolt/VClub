package com.easyder.club.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.easyder.club.R;
import com.easyder.club.module.common.MainActivity;
import com.easyder.club.module.me.order.ShopOrderFragment;
import com.lzy.okgo.OkGo;
import com.sky.wrapper.utils.LanguageUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: sky on 2020/11/13 18:06
 * Email: xcode126@126.com
 * Desc:
 */
public class OperateUtils {

    private static OperateUtils instance;

    private OperateUtils() {
    }

    public synchronized static OperateUtils getInstance() {
        if (instance == null) {
            instance = new OperateUtils();
        }
        return instance;
    }

    public static List<String> getTestList() {
        List list = new ArrayList<>();
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        list.add("1");
        return list;
    }

    /**
     * 把字符串解析成图片数组
     *
     * @param str
     * @return
     */
    public static List<String> getImageArray(String str) {
        if (TextUtils.isEmpty(str)) {
            return new ArrayList<>();
        }
        String[] split = str.split(";");
        if (split != null) {
            return Arrays.asList(split);
        }
        return new ArrayList<>();
    }

    /**
     * get first image
     *
     * @param imageUrl
     * @return
     */
    public static String getFirstImage(String imageUrl) {
        if (imageUrl != null && imageUrl.contains(";")) {
            String[] split = imageUrl.split(";");
            return split[0];
        }
        return imageUrl;
    }

    /**
     * get status
     *
     * @param orderstate
     * @param mContext
     * @return
     */
    public String getStatus(Context mContext, int orderstate) {
        if (orderstate == ShopOrderFragment.ORDER_STATE_UNPAID) {//待支付
            return mContext.getString(R.string.a_wait_pay);
        } else if (orderstate == ShopOrderFragment.ORDER_STATE_WAIT_SEND) {//待发货
            return mContext.getString(R.string.a_wait_send);
        } else if (orderstate == ShopOrderFragment.ORDER_STATE_WAIT_RECEIVE) {//待收货
            return mContext.getString(R.string.a_wait_receive);
        } else if (orderstate == ShopOrderFragment.ORDER_STATE_WAIT_EVALUATE) {//待评价
            return mContext.getString(R.string.a_wait_evaluate);
        } else if (orderstate == ShopOrderFragment.ORDER_STATE_YET_COMPLETE) {//已完成
            return mContext.getString(R.string.a_yet_complete);
        } else if (orderstate == ShopOrderFragment.ORDER_STATE_YET_CANCEL) {//已取消
            return mContext.getString(R.string.a_yet_cancel);
        } else if (orderstate == ShopOrderFragment.ORDER_STATE_PAY_CANCEL) {//支付后又取消订单了
            return mContext.getString(R.string.a_pay_after_cancel);
        } else if (orderstate == ShopOrderFragment.ORDER_STATE_YET_EXIT) {//全部产品已退款
            return mContext.getString(R.string.a_all_yet_exit_money);
        } else if (orderstate == ShopOrderFragment.ORDER_STATE_YET_HALF_EXIT) {//部分产品已退款
            return mContext.getString(R.string.a_half_yet_exit_money);
        }
        else {
            return "";
        }
    }

    /**
     * @param context
     */
    public static void switchLanguage(Activity context) {
        LanguageUtils.switchLanguage(context);
        OkGo.getInstance().getCookieJar().getCookieStore().removeAllCookie();
        context.startActivity(MainActivity.getResetIntent(context));
//        WrapperApplication.initNetConfig();
    }

    /**
     * @param context
     */
    public static void switchLanguage(Activity context, String language) {
        if (TextUtils.isEmpty(language)) {
            LanguageUtils.switchLanguage(context);
        } else {
            LanguageUtils.applyLanguage(context, language);
        }
        OkGo.getInstance().getCookieJar().getCookieStore().removeAllCookie();
        context.startActivity(MainActivity.getResetIntent(context));
//        WrapperApplication.initNetConfig();
    }
}
