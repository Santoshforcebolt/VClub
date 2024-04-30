package com.easyder.club.module.basic.event;

/**
 * Author:  winds
 * Data:    2018/7/27
 * Version: 1.0
 * Desc:
 */


public class OrderChanged {
    public int sign;
    public OrderIml iml;

    public OrderChanged(int sign) {
        this.sign = sign;
    }

    public OrderChanged(int sign, OrderIml iml) {
        this.sign = sign;
        this.iml = iml;
    }
}
