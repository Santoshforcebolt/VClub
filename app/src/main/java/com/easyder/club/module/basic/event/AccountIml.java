package com.easyder.club.module.basic.event;

/**
 * Author:  winds
 * Data:    2018/7/7
 * Version: 1.0
 * Desc:
 */


public interface AccountIml {
    int ACCOUNT_REQISTER = 10010;   //注册
    int ACCOUNT_LOGIN = 10011;      //登录
    int ACCOUNT_LOGOUT = 10012;       //退出

    int ACCOUNT_PERSON_CHANGE = 10013;//个人信息更改
    int ACCOUNT_CAR_CHANGE = 10014;//购物车更改
    int ACCOUNT_INTEGRAL_CHANGE = 10015;//积分更改
    int ACCOUNT_ENJOY_CHANGE = 10016;//评鉴状态更改
    int ACCOUNT_PUBLISH_ENJOY = 10017;//发表评鉴成功
    int ACCOUNT_WALLET_CHANGE = 10018;//钱包金额更改

}
