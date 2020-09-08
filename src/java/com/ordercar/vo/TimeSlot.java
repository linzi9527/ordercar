package com.ordercar.vo;


import com.summaryday.framework.a.Colum;
import com.summaryday.framework.a.Key;
import com.summaryday.framework.a.Table;
import lombok.Data;

/**
 * 时间段信息表
 */
@Data
@Table(name="tbl_time_slot",type= Table.policy.VO)
public class TimeSlot {

    @Key(isPrimary=true)
    @Colum(columName="id",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String id;
    @Colum(columName="drivingId",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String drivingId; //驾校id
    @Colum(columName="type",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String type;//科目:0-科目二 1-科目三
    @Colum(columName="startTime",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String startTime; //开始时间段
    @Colum(columName="endTime",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String endTime; //结束时间段
    @Colum(columName="openTime",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String openTime;//启用时间
    @Colum(columName="status",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String status; //0停用，1启用
    @Colum(columName="createTime",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String createTime;//创建时间
}
