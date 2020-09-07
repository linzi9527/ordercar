package com.ordercar.vo;


import com.summaryday.framework.a.Colum;
import com.summaryday.framework.a.Key;
import com.summaryday.framework.a.Table;
import lombok.Data;

/**
 * 驾校基本信息和账号信息
 */
@Data
@Table(name="tbl_drivingaccount",type= Table.policy.VO)
public class DrivingAccount {

    @Key(isPrimary=true)
    @Colum(columName="id",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String id;

    @Colum(columName="drivingname",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String drivingname;

    @Colum(columName="concat",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String concat;

    @Colum(columName="phone",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String phone;

    @Colum(columName="wxno",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String wxno;

    @Colum(columName="address",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String address;

    @Colum(columName="appid",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String appid;

    @Colum(columName="appkey",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String appkey;


    @Colum(columName="account",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String account;

    @Colum(columName="password",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String password;

    @Colum(columName="status",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String status;

    @Colum(columName="createdate",isNUll=false,type= Colum.ObjTypes.VARCHAR)
    private String createdate;
}
