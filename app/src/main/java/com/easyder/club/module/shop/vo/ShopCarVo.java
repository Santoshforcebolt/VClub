package com.easyder.club.module.shop.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/11/26 18:17
 * Email: xcode126@126.com
 * Desc:
 */
public class ShopCarVo extends BaseVo {

    @Override
    public void setDataList(List<?> list) {
        this.rows= (List<RowsBean>) list;
    }

    @Override
    public Class<?> elementType() {
        return ShopCarVo.RowsBean.class;
    }

    public List<RowsBean> rows;

    public static class RowsBean {
        public int availablestocknum;
        public String brand;
        public int customercode;
        public long gmtcreate;
        public int id;
        public String imgurl;
        public String itemcode;
        public String itemname;
        public String itemtype;
        public String masterid;
        public int number;
        public double price;
        public String productgroup;
        public int state;
    }
}
