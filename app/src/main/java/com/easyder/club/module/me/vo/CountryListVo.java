package com.easyder.club.module.me.vo;

import android.text.TextUtils;

import com.easyder.club.utils.PinYinUtils;
import com.sky.wrapper.core.model.BaseVo;

import java.io.Serializable;
import java.util.List;

/**
 * Author: sky on 2020/12/2 16:36
 * Email: xcode126@126.com
 * Desc:
 */
public class CountryListVo extends BaseVo {

    @Override
    public void setDataList(List<?> list) {
        this.rows = (List<RowsBean>) list;
    }

    @Override
    public Class<?> elementType() {
        return CountryListVo.RowsBean.class;
    }

    public List<RowsBean> rows;

    public static class RowsBean implements Serializable {
        public String code;
        public int countrycode;
        public String countryname;

        private String pinyin;

        public String getPinyin() {
            if (TextUtils.isEmpty(countryname)){
                return "";
            }
            return  PinYinUtils.getPinYin(countryname);
        }
    }
}
