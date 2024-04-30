package com.easyder.club.module.me.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/12/9 16:03
 * Email: xcode126@126.com
 * Desc:
 */
public class AfterSaleCalculatorVo extends BaseVo {
    public double actualmoney;
    public List<PiDetailedBean> piDetailed;
    public static class PiDetailedBean {
        public int actualmoney;
        public int id;
        public int number;
        public int orderdetailed;
        public String orderno;
        public String picode;
        public String piimgurl;
        public String piname;
        public String pitype;
    }
}
