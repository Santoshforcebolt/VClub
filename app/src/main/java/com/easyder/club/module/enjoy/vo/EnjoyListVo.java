package com.easyder.club.module.enjoy.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/12/1 17:48
 * Email: xcode126@126.com
 * Desc:
 */
public class EnjoyListVo extends BaseVo {

    public int wish;
    public int sumtotal;
    public int total;
    public int collection;

    public List<RowsBean> rows;

    public static class RowsBean {
        public String address;
        public int collection;
        public String colorcomment;
        public int colorscore;
        public String comment;
        public String createdate;
        public int alcoholic;
        public String bottled;
        public String distillationyear;
        public int capacity;
        public int customercode;
        public String customername;
        public String endingcomment;
        public int endingscore;
        public String icourl;
        public int id;
        public String images;
        public String imgurl;
        public String productcode;
        public String productname;
        public String smellcomment;
        public int smellscore;
        public String tastecomment;
        public int tastescore;
        public int totalscore;
        public int wish;
    }
}
