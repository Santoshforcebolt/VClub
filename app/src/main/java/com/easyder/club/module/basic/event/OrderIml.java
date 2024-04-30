package com.easyder.club.module.basic.event;

/**
 * Author:  winds
 * Data:    2018/7/27
 * Version: 1.0
 * Desc:
 */


public interface OrderIml {
    int SIGN_ORDER_COMMIT_SUCCESS = 1;//订单提交成功
    int SIGN_ORDER_PAY_SUCCESS = 2;//订单支付成功
    int SIGN_AFTER_SALE_STATE_CHANGE = 3;//售后订单状态发生改变
    int SIGN_APPLY_AFTER_SALE_SUCCESS = 4;//申请售后成功

}
