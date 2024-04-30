package com.easyder.club.module.shop.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/12/7 16:03
 * Email: xcode126@126.com
 * Desc:
 */
public class KindVo extends BaseVo {

    public int total;
    public String isswitch;
    public List<DataBean> data;

    public static class DataBean {
        public String adddatetime;
        public String addemp;
        public int attributetype;
        public boolean deleted;
        public int delflag;
        public int groupcode;
        public String groupname;
        public String grouptype;
        public String icourl;
        public String masterid;
        public String modifydatetime;
        public String modifyemp;
        public String orderremark;
        public int sort;
        public int state;
        public List<?> productList;
        public List<?> serviceItemList;
    }
}
