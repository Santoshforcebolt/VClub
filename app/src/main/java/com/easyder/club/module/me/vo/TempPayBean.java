package com.easyder.club.module.me.vo;

import com.easyder.club.module.common.vo.BaseTempBean;

/**
 * Author: sky on 2020/11/30 15:37
 * Email: xcode126@126.com
 * Desc:
 */
public class TempPayBean extends BaseTempBean {

    public static final int TEMP_PAY_SHOP_CAR = 1;//购物车下单支付
    public static final int TEMP_PAY_SHOP_ORDER = 2;//商城订单列表支付
//    public static final int TEMP_PAY_OPEN_GROUP = 3; //开团
//    public static final int TEMP_PAY_JOIN_GROUP = 4; //参团
//    public static final int TEMP_PAY_GROUP_CONTINUE_PAY = 5; //拼团继续支付

    public int type;//类型
    public String orderNo; //订单号
    public double needAmount;//需要支付的金额

    public TempPayBean(int type, String orderNo, double needAmount) {
        this.type = type;
        this.orderNo = orderNo;
        this.needAmount = needAmount;
    }

}
