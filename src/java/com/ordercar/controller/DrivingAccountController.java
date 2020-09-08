package com.ordercar.controller;

import com.ordercar.utils.AllowOrigin;
import com.ordercar.vo.DrivingAccount;
import com.summaryday.framework.d.IBaseDao;
import com.summaryday.framework.db.DateUtil;
import com.summaryday.framework.db.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 后台维护驾校基本信息和账号信息
 *
 */

@Slf4j
@Controller
@RequestMapping("/v1")
public class DrivingAccountController {

    @Autowired
    private IBaseDao baseDao;

    //接口数据封装类，返回给前端的数据格式
    private Map<String,Object> resultData=new HashMap<String,Object>();
    /**
     * 前台传递的方式是json
     * 当需要传递Json格式的数据是，后端接收的方法@RequestBody
     * 返回数据@ResponseBody即以json格式返回Map<String,Object> resultData
     * 415问题已经处理
     */
    @RequestMapping(value = "/addAccount",method = RequestMethod.POST,consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    //@RequestMapping("/addAccount")
    @ResponseBody
    public Map<String,Object> Account(DrivingAccount drivingAccount, HttpServletResponse resp){
    //public Map<String,Object> Account(@RequestBody DrivingAccount drivingAccount, HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        log.info("drivingAccount:"+drivingAccount);
        resultData.clear();
        resultData.put("code", 400);//失败
        resultData.put("info", "操作失败！");
        if(drivingAccount!=null){
            try {
                drivingAccount.setId(UUID.randomUUID().toString().replaceAll("-",""));
                drivingAccount.setStatus("1");
                drivingAccount.setCreatedate(DateUtil.getCurDate());
                baseDao.save(drivingAccount);
                resultData.put("code", 200);//成功
                resultData.put("info", "操作成功！");
            } catch (Exception e) {
               log.error("新增驾校账户信息异常："+e);
            }
        }
        return resultData;
    }

    //驾校维护列表

    /**
     *
     * @param searchKey
     * @param startIndex 第几页
     * @param count 每页条数
     * @param resp
     * @return
     */
    @RequestMapping(value = "/listAccount")
    @ResponseBody
    public Map<String,Object> Account(String searchKey,int startIndex,int count, HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        log.info("searchKey:"+searchKey);
        resultData.clear();
        resultData.put("code", 400);//失败
        resultData.put("info", "操作失败！");
        String SQL="",limit="";
        if(startIndex>=0&&count>0){
            startIndex=startIndex*count;//当前从那条数据开始，向后找count条
            limit="  limit "+startIndex+","+count;
        }
        if(StringUtil.isEmpty(searchKey)){
            SQL=" where 1=1 order by createdate desc";
        }else{
            SQL=" where drivingname like '%"+searchKey+"%' OR concat like '%"+searchKey+"%' OR phone like'%"+searchKey+"%' order by createdate desc ";
        }
        try {
            List<DrivingAccount> list= (List<DrivingAccount>) baseDao.queryList(DrivingAccount.class,SQL+limit,false);
            resultData.put("code", 200);//成功
            resultData.put("info", "操作成功！");
            resultData.put("list", list);
            resultData.put("datacount", 0);
            if(list!=null) {
                List<DrivingAccount> listp = (List<DrivingAccount>) baseDao.queryList(DrivingAccount.class, SQL, false);
                resultData.put("datacount",  listp.size());
            }
        } catch (Exception e) {
            log.error("驾校账户列表信息异常："+e);
        }
        return resultData;
    }


    //根据id查看驾校基本信息
    @RequestMapping(value = "/getAccountInfo")
    @ResponseBody
    public Map<String,Object> getAccountInfo(String id, HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        log.info("根据id查看驾校基本信息id:"+id);
        resultData.clear();
        resultData.put("code", 400);//失败
        resultData.put("info", "操作失败！");
        if(!StringUtil.isEmpty(id)){
            try {
                DrivingAccount drivingAccount= (DrivingAccount) baseDao.get(id,DrivingAccount.class,false);
                resultData.put("code", 200);//成功
                resultData.put("info", "操作成功！");
                resultData.put("drivingAccount", drivingAccount);
            } catch (Exception e) {
                log.error("根据id查看驾校基本信息异常："+e);
            }
        }
        return resultData;
    }




}
