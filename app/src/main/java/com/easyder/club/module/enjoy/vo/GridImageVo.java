package com.easyder.club.module.enjoy.vo;


import com.sky.wrapper.core.model.BaseVo;

/**
 * Author：sky on 2019/12/4 21:36.
 * Email：xcode126@126.com
 * Desc：
 */

public class GridImageVo extends BaseVo {

    public boolean isAdd;
    public String path;

    public GridImageVo(String path) {
        this.path = path;
    }

    public GridImageVo(boolean isAdd, String path) {
        this.isAdd = isAdd;
        this.path = path;
    }
}
