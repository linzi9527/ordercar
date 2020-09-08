package com.ordercar.vo;


import lombok.Data;

/**
 * 车辆列表所有条件参数
 */
@Data
public class CarParams {

    private String km;//科目二、科目三
    private String isOrderNo;//是否排序:1使用排序，0不使用
    private String searchKey;//搜素关键词
    private String carType;//车辆类型C1、C2
    private String status;//1正常，0停用
    private int startIndex;//起始下标
    private int count;//每页记录数
}
