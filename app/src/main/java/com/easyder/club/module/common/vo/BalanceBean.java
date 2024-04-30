package com.easyder.club.module.common.vo;

import com.easyder.club.module.basic.event.AccountIml;

/**
 * Author: sky on 2020/12/17 15:33
 * Email: xcode126@126.com
 * Desc:
 */
public class BalanceBean implements AccountIml {
    public double balance;

    public BalanceBean(double balance) {
        this.balance = balance;
    }
}
