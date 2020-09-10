package com.ordercar.vo;

import lombok.Data;

@Data
public class CarInfoVo {
    private String id;//车辆ID
    private String carimg;//图片
    private String carname;// 车的名称
    private String cartype;//车的类型
    private String historyNumber;//历史预约量
    private String surplusNumber;//剩余号源
}
