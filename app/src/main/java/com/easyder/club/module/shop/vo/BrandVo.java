package com.easyder.club.module.shop.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/12/7 16:32
 * Email: xcode126@126.com
 * Desc:
 */
public class BrandVo extends BaseVo {

    @Override
    public void setDataList(List<?> list) {
        this.rows= (List<RowsBean>) list;
    }

    @Override
    public Class<?> elementType() {
        return BrandVo.RowsBean.class;
    }

    public List<RowsBean> rows;

    public static class RowsBean {
        public String adddatetime;
        public String addemp;
        public int brandcode;
        public String brandname;
        public boolean deleted;
        public int delflag;
        public String icourl;
        public String masterid;
        public String modifydatetime;
        public String modifyemp;
        public int oldbrandcode;
        public int sort;
        public int state;
    }
}
