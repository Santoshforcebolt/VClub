package com.easyder.club.module.me.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/11/26 15:13
 * Email: xcode126@126.com
 * Desc:
 */
public class SpreadHistoryVo extends BaseVo {

    public int total;
    public List<ListBean> list;

    public static class ListBean {
        public String commdate;
        public Object commissdate;
        public double commission;
        public String commissname;
        public Object commisstime;
        public String commisstype;
        public String commtime;
        public String createdate;
        public String deptname;
        public String icourl;
        public int id;
        public int level;
        public int levelratio;
        public String masterid;
        public String orderno;
        public int ratio;
        public double salemoney;
        public int scustomercode;
        public String scustomername;
        public String showval;
        public int tcustomercode;
        public String tcustomername;
        public String tel;
    }
}
