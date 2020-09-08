package com.ordercar.vo;


import com.summaryday.framework.a.Colum;
import com.summaryday.framework.a.Key;
import com.summaryday.framework.a.Table;
import lombok.Data;

/**
 * 时间段设置信息表
 */
@Data
@Table(name="tbl_time_set",type= Table.policy.VO)
public class TimeSet {

    @Key(isPrimary=true)
    @Colum(columName="id",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String id;
    @Colum(columName="drivingId",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String drivingId; //驾校id
    @Colum(columName="isNumber",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String isNumber;//是否允许多个时间段：0-否 1-是
    @Colum(columName="number",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String number;//允许几个时间段
    @Colum(columName="time",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String time;//最多显示几档期
    @Colum(columName="createTime",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String createTime;//创建时间
}
