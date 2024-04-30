package com.easyder.club.module.shop.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/11/24 18:45
 * Email: xcode126@126.com
 * Desc:
 */
public class ShopRecommendVo extends BaseVo {

    public int total;
    public List<RowsBean> rows;

    public static class RowsBean {
        public String gmtcreate;
        public String gmtmodified;
        public int id;
        public String imgurl;
        public double marketprice;
        public String masterid;
        public String piccode;
        public String picname;
        public String pictype;
        public int salenum;
        public double totalscore;
        public double saleprice;
        public int sort;
        public int state;
        public int collectnumber;
        public int stocknum;
    }
}
