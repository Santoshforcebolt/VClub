package com.easyder.club.module.shop.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/11/24 15:38
 * Email: xcode126@126.com
 * Desc:
 */
public class GetTicketCenterVo extends BaseVo {

    public int total;
    public List<CouponCashsBean> couponCashs;

    public static class CouponCashsBean {
        public String addtime;
        public String brand;
        public int cashmoney;
        public String couponcode;
        public String couponname;
        public String coupontype;
        public String deptcode;
        public int donation;
        public String effectiverange;
        public String effectiverangename;
        public String effenum;
        public int eusenum;
        public int fullmoney;
        public String itemname;
        public String picode;
        public String pictype;
        public String productgroup;
        public int receive;
        public int receivednumber;
        public String showname;
        public int state;
        public int superposition;
        public List<?> productList;
        public List<?> serviceItemList;
    }
}
