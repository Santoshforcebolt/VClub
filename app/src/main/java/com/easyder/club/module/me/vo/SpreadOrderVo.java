package com.easyder.club.module.me.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/11/26 15:28
 * Email: xcode126@126.com
 * Desc:
 */
public class SpreadOrderVo extends BaseVo {
    public int total;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * commdate : 2019-06-25
         * commissdate : 1561392000000
         * commission : 67.8
         * commissname : 新开卡
         * commisstime : 1561426980000
         * commisstype : newcardorder
         * commtime : 2019-06-25 09:43:00
         * createdate :
         * deptname : 依斯卡总部
         * icourl :
         * id : 98
         * level : 1
         * levelratio : 0
         * optId : 0
         * orderno : 1906250942537
         * ratio : 0
         * salemoney : 226
         * scustomercode : 135
         * scustomername : sdsdsd
         * showval : +67.80
         * tcustomercode : 78
         * tcustomername : 13717347806
         * tel : 13717347806
         */

        public String commdate;
        public long commissdate;
        public double commission;
        public String commissname;
        public long commisstime;
        public String commisstype;
        public String commtime;
        public String createdate;
        public String deptname;
        public String icourl;
        public int id;
        public int level;
        public int levelratio;
        public int optId;
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
