package com.sky.wrapper.core.model;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import org.json.JSONException;
import java.io.Serializable;
import java.util.List;

/**
 * @author 刘琛慧
 * date 2015/10/13.
 */
public class BaseVo implements Serializable {
    public List<? extends Entity> queryData;
    public boolean dataFromDB;


    /**
     * 根据对象类型解析数据 如果对象类型是数组 子类需实现elementType和setDataList方法
     *
     * @param jsonString Json字符串
     * @param dataClass  对象类型
     * @param <T>        对象泛型
     * @return
     * @throws JSONException
     */
    public static <T extends BaseVo> T parseDataVo(String jsonString, Class<T> dataClass) throws JSONException {
        if (TextUtils.isEmpty(jsonString) || dataClass == null) {
            return null;
        }
        T baseVo;
        try {
            if (jsonString.startsWith("[")) {
                baseVo = dataClass.newInstance();
                List<?> dataSet = JSON.parseArray(jsonString, baseVo.elementType());
                baseVo.setDataList(dataSet);
                baseVo.doMock();
            } else if (jsonString.startsWith("{")) {
                baseVo = JSON.parseObject(jsonString, dataClass);
            } else {
                //{"msg":"jsonString"}
                jsonString = "{" + "\""+"msg"+"\""+":" +"\""+ jsonString + "\""+ "}";
                baseVo = JSON.parseObject(jsonString, dataClass);
//                baseVo = dataClass.newInstance();
            }
        } catch (Exception ex) {
            throw new JSONException("数据解析失败! Exception: " + ex.getMessage());
        }
        return baseVo;
    }

    /**
     * 如果返回数据是集合，数据解析方法会调用次方法获取元素的类型
     *
     * @return
     */
    public Class<?> elementType() {
        return null;
    }


    /**
     * 数据解析成功后会调用此方法设置数据
     *
     * @param list
     */
    public void setDataList(List<?> list) {

    }


    /**
     * 执行数据模拟，在RequestInfo.needMockData为true的情况下，框架层会调用此方法，
     * 子类在此方法时实例化自己的对象和参数
     */
    public void doMock() {

    }

    public boolean isDataFromDB() {
        return dataFromDB;
    }

    public void setDataFromDB(boolean dataFromDB) {
        this.dataFromDB = dataFromDB;
    }

    public void formData(List<? extends Entity> queryData) {
        this.queryData = queryData;
    }

    public List<? extends Entity> getQueryData() {
        return queryData;
    }
}

