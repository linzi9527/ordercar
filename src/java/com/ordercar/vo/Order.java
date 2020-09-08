package com.ordercar.vo;

import com.summaryday.framework.a.Colum;
import com.summaryday.framework.a.Key;
import com.summaryday.framework.a.Table;
import lombok.Data;

/**
 * 订单信息表
 */
@Data
@Table(name="tbl_order",type= Table.policy.VO)
public class Order {
    @Key(isPrimary=true)
    @Colum(columName="id",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String id;
    @Colum(columName="drivingId",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String drivingId; //驾校id
    @Colum(columName="orderId",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String orderId;//订单id
    @Colum(columName="type",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String type;//科目:0-科目二 1-科目三
    @Colum(columName="timeSlotId",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String timeSlotId; //预约时段
    @Colum(columName="carinfoId",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String carinfoId;// 车辆ID
    @Colum(columName="name",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String	name;//姓名
    @Colum(columName="tel",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String	tel;//电话
    @Colum(columName="time",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String	time;//预约时间
    @Colum(columName="status",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String  status;//0已取消，1已预约
    @Colum(columName="remarks",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String remarks;//备注
    @Colum(columName="createTime",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String  createTime;//创建时间
}
