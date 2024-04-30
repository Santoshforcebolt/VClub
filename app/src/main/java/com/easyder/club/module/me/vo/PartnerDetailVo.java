package com.easyder.club.module.me.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author：sky on 2019/6/25 16:07.
 * Email：xcode126@126.com
 * Desc：礼品券商家详情
 */

public class PartnerDetailVo extends BaseVo {

    public List<DatasBean> datas;

    public static class DatasBean {
        public String addemp;
        public String address;
        public String addtime;
        public int delflag;
        public int id;
        public String linkman;
        public int partnercode;
        public String shopname;
        public int state;
        public String tel;
        public String updateemp;
        public String updatetime;
    }
}
