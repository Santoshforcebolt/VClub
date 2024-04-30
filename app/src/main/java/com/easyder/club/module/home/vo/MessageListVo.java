package com.easyder.club.module.home.vo;


import com.sky.wrapper.core.model.BaseVo;

import java.util.List;

/**
 * Author: sky on 2020/11/23 11:57
 * Email: xcode126@126.com
 * Desc:
 */
public class MessageListVo extends BaseVo {

    public int total;
    public List<RowsBean> rows;

    public static class RowsBean {
        public String content;
        public String contentBrief;
        public String gmtcreate;
        public String gmtmodified;
        public int id;
        public String masterid;
        public int state;
        public String title;
    }
}
