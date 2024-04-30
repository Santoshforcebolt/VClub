package com.easyder.club.module.me.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author：sky on 2019/6/12 11:38.
 * Email：xcode126@126.com
 * Desc：省市区信息
 */

public class RegionVo extends BaseVo {

    @Override
    public void setDataList(List<?> list) {
        this.rows = (List<RowsBean>) list;
    }

    @Override
    public Class<?> elementType() {
        return RowsBean.class;
    }

    public List<RowsBean> rows;

    public static class RowsBean {
        public String code;
        public int id;
        public String name;
        public int parentid;
        public String path;
        public int type;
    }
}
