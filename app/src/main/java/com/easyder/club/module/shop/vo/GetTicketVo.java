package com.easyder.club.module.shop.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/11/30 10:36
 * Email: xcode126@126.com
 * Desc:
 */
public class GetTicketVo extends BaseVo {

    public int total;
    public List<RowsBean> rows;

    public static class RowsBean {
        public String addtime;
        public String brand;
        public int cashmoney;
        public int couponcode;
        public String couponname;
        public String coupontype;
        public String deptcode;
        public int donation;
        public String effectiverange;
        public String effectiverangename;
        public int effenum;
        public int eusenum;
        public int fullmoney;
        public String itemname;
        public String masterid;
        public String picode;
        public String pictype;
        public String productgroup;
        public int receive;
        public int receivednumber;
        public String showname;
        public int state;
        public int superposition;
    }
}
