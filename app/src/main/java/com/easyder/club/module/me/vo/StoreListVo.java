package com.easyder.club.module.me.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/11/23 15:15
 * Email: xcode126@126.com
 * Desc:
 */
public class StoreListVo extends BaseVo {

    @Override
    public void setDataList(List<?> list) {
        this.rows = (List<RowsBean>) list;
    }

    @Override
    public Class<?> elementType() {
        return StoreListVo.RowsBean.class;
    }

    public List<RowsBean> rows;

    public static class RowsBean {
        public String adddatetime;
        public String addemp;
        public String addr;
        public int appointment;
        public String businesstime;
        public int delflag;
        public int deptcode;
        public String deptname;
        public int discount;
        public int distance;
        public int empconut;
        public String enddatetime;
        public int grade;
        public String imgurl;
        public int lat;
        public String leaf;
        public int level;
        public int lng;
        public String masterid;
        public String modifydatetime;
        public String modifyemp;
        public String nature;
        public int parentcode;
        public String parentname;
        public String parentpath;
        public String path;
        public int score;
        public int shopconut;
        public int sort;
        public String tage;
        public String tel;
        public List<?> children;
    }
}
