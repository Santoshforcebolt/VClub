package com.easyder.club.module.me.vo;

import com.easyder.club.module.common.vo.BaseTempBean;

import java.util.List;

/**
 * Author: sky on 2020/12/10 18:38
 * Email: xcode126@126.com
 * Desc:
 */
public class TempApplyBean extends BaseTempBean{
    public int orderstate;
    public String orderno;
    public List<OrderDetailVo.DetailedListBean> detailedList;

    public TempApplyBean(int orderstate, String orderno) {
        this.orderstate = orderstate;
        this.orderno = orderno;
    }
}
