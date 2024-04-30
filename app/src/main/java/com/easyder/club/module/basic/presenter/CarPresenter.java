package com.easyder.club.module.basic.presenter;

import com.alibaba.fastjson.JSONObject;
import com.easyder.club.ApiConfig;
import com.easyder.club.module.shop.vo.CalculateOrderVo;
import com.easyder.club.module.shop.vo.TempCarBean;
import com.easyder.club.utils.RequestParams;
import com.sky.wrapper.core.model.BaseVo;

/**
 * Author: sky on 2020/11/26 17:59
 * Email: xcode126@126.com
 * Desc:
 */
public class CarPresenter extends CommonPresenter {

    /**
     * 加入购物车
     */
    public void addCar(String itemcode, int number) {
        addCar("product", itemcode, number);
    }

    /**
     * 加入购物车
     */
    private void addCar(String itemtype, String itemcode, int number) {
        TempCarBean bean = new TempCarBean(itemcode, number, itemtype);
        String shopcarts = String.format("[%1$s]", JSONObject.toJSONString(bean));
        addCar(shopcarts);
    }

    /**
     * 加入购物车
     *
     * @param shopcarts
     */
    private void addCar(String shopcarts) {
//        setNeedDialog(true);
        postData(ApiConfig.API_ADD_CAR, new RequestParams()
                .putCid().put("shopcarts", shopcarts)
                .get(), BaseVo.class);
    }

    /**
     * @param id
     * @param number
     */
    public void updateCar(int id, int number) {
        postData(ApiConfig.API_UPDATE_CAR, new RequestParams()
                .putCid()
                .put("id", id)
                .put("number", number)
                .get(), BaseVo.class);
    }

    /**
     * @param ids
     */
    public void deleteCar(String ids) {
        postData(ApiConfig.API_DELETE_CAR, new RequestParams()
                .put("ids", ids)
                .putCid()
                .get(), BaseVo.class);
    }

    /**
     * 计算价格
     *
     * @param orderJsonStr
     */
    public void calculatePrice(String orderJsonStr) {
        postData(ApiConfig.API_CALCULATE_PRICE, new RequestParams()
                .put("orderJsonStr", orderJsonStr)
                .putCid()
                .get(), CalculateOrderVo.class);
    }
}
