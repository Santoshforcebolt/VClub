package com.easyder.club.module.me.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.io.Serializable;
import java.util.List;

/**
 * Author：sky on 2019/6/12 10:41.
 * Email：xcode126@126.com
 * Desc：地址列表
 */

public class AddressListVo extends BaseVo {

    @Override
    public void setDataList(List<?> list) {
        this.rows = (List<RowsBean>) list;
    }

    @Override
    public Class<?> elementType() {
        return AddressListVo.RowsBean.class;
    }

    public List<RowsBean> rows;

    public static class RowsBean implements Serializable{
        public String addflag;
//        public String addresscode;
//        public String addressname;
        public int customercode;
        public int defaultflag;
        public String detailedaddre;
        public String id;
        public String receivername;
        public String receivertel;
        public String zipcode;
    }

}
