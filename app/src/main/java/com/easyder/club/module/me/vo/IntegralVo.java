package com.easyder.club.module.me.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/11/26 16:05
 * Email: xcode126@126.com
 * Desc:
 */
public class IntegralVo extends BaseVo {
    public int total;
    public List<RowsBean> rows;

    public static class RowsBean {
        public String exchangeobject;
        public String imgurl;
        public String itemcode;
        public String itemtype;
        public String moddatetime;
        public int score;
        public String  partnercode;
        public double saleprice;
        public String scoreproductcode;
        public String scoreproductname;
        public String stocknum;
        public String useexchangenum;
    }
}
