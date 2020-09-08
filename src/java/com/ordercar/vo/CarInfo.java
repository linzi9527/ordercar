package com.ordercar.vo;


import com.summaryday.framework.a.Colum;
import com.summaryday.framework.a.Key;
import com.summaryday.framework.a.Table;
import lombok.Data;

/**
 * 驾校车辆信息
 */
@Data
@Table(name="tbl_carinfo",type= Table.policy.VO)
public class CarInfo {

    @Key(isPrimary=true)
    @Colum(columName="id",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String id;

    @Colum(columName="carname",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String carname;

    @Colum(columName="carimg",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String carimg;

    @Colum(columName="km",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String km;

    @Colum(columName="cartype",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String cartype;

    @Colum(columName="orderno",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String orderno;

    @Colum(columName="createtime",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String createtime;

    @Colum(columName="status",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String status;

    @Colum(columName="belong",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String belong;
}
