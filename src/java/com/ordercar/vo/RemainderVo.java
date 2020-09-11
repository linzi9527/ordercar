package com.ordercar.vo;

import lombok.Data;

@Data
public class RemainderVo {
    private String id;//车辆ID
    private String carname;//车辆名称
    private String status;//车辆状态
    private String allNumber;//全部号源
    private String reservedNumber;//已预约
    private String surplusNumber;//剩余号源
}
