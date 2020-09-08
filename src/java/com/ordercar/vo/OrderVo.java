package com.ordercar.vo;

import lombok.Data;

@Data
public class OrderVo {
    private String id;
    private String orderId;//订单id
    private String name;//姓名
    private String tel;//电话
    private String carimg;//图片
    private String carname;// 车的名称
    private String cartype;//车的类型
    private String startTime;//开始时间
    private String endTime;// 结束时间
    private String time;//预约时间
    private String status;//0已取消，1已预约
    private String remarks;//备注
}
