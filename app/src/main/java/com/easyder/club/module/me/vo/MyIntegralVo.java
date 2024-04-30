package com.easyder.club.module.me.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author：sky on 2019/6/12 18:00.
 * Email：xcode126@126.com
 * Desc：积分明细
 */

public class MyIntegralVo extends BaseVo {

    public int total;
    public List<RowsBean> rows;

    public static class RowsBean {
        public String changedate;
        public String changename;
        public String changetime;
        public String changetype;
        public String describes;
        public int id;
        public String masterid;
        public int newscore;
        public String objecttype;
        public int oldscore;
        public String orderno;
        public String remark;
        public int score;
        public String showval;
        public int vipempcode;
    }
}
