package com.easyder.club.module.basic.event;

/**
 * Author:  winds
 * Data:    2018/7/7
 * Version: 1.0
 * Desc:
 */


public class AccountChanged {
    public int sign;
    public AccountIml iml;

    public AccountChanged(int sign) {
        this.sign = sign;
    }

    public AccountChanged(int sign, AccountIml iml) {
        this.sign = sign;
        this.iml = iml;
    }
}
