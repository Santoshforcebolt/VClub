package com.easyder.club.module.me.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/11/23 14:05
 * Email: xcode126@126.com
 * Desc:
 */
public class AppointListVo extends BaseVo {

    public int total;
    public List<RowsBean> rows;

    public static class RowsBean {
        public String appointmenttime;
        public int box;
        public int customercode;
        public String customername;
        public String deptaddress;
        public int deptcode;
        public String deptimgurl;
        public String deptname;
        public String empname;
        public int empno;
        public String icourl;
        public int itemnum;
        public String masterid;
        public int online;
        public String orderdate;
        public String orderno;
        public int orderstate;
        public String ordertime;
        public String overduetime;
        public String remark;
        public int servicetimelong;
        public String tel;
        public int toshopnumber;
    }
}
