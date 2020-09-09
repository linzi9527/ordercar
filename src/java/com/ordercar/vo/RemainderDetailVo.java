package com.ordercar.vo;

import lombok.Data;

@Data
public class RemainderDetailVo {
    private String id;//预约时段ID
    private String startTime;//开始时间
    private String endTime;//结算时间
    private String reservedNumber;//已预约

}
