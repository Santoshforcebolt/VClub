package com.easyder.club.module.shop.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Author：sky on 2019/6/17 15:31.
 * Email：xcode126@126.com
 * Desc：计算价格参数
 */

public class TempCalculateVo {
    public List<DetailedListBean> detailedList;
    public List<String> selectedCoupon;
    public List<Integer> selectedPackageGroup;
    public String orderno;
    public String actualmoney;
    public String remark;
    public String addressid;


    public static class DetailedListBean implements Serializable {
        public String type;
        public String code;
        public int number;

        public DetailedListBean() {

        }

        public DetailedListBean(String code, int number) {
            this.type = "product";
            this.code = code;
            this.number = number;
        }

        public DetailedListBean(String type, String code, int number) {
            this.type = type;
            this.code = code;
            this.number = number;
        }
    }

    public TempCalculateVo() {

    }
}
