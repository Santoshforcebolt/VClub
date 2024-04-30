package com.easyder.club.module.me.vo;

import com.easyder.club.module.common.vo.BaseTempBean;

import java.util.List;

/**
 * Author: sky on 2020/11/30 15:29
 * Email: xcode126@126.com
 * Desc:
 */
public class TempCommitOrderBean extends BaseTempBean {
    public String orderno;
    public String actualmoney;
    public String addressid;
    public String remark;
    public String deduction;
    public List<DetailedListBean> detailedList;
    public List<String> selectedCoupon;
    public List<Integer> selectedPackageGroup;

    public static class DetailedListBean {
        public String type;
        public String code;
        public int number;

        public DetailedListBean(String code, int number) {
            this.type = "product";
            this.code = code;
            this.number = number;
        }
    }

    public TempCommitOrderBean(String orderno) {
        this.orderno = orderno;
    }
}
