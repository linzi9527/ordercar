package com.ordercar.controller;


import com.ordercar.utils.AllowOrigin;
import com.ordercar.vo.DrivingAccount;
import com.ordercar.vo.TimeSet;
import com.summaryday.framework.d.IBaseDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * 登录
 */
@Slf4j
@Controller
@RequestMapping("/v1")
public class LoginController {


    @Autowired
    private IBaseDao baseDao;

    //接口数据封装类，返回给前端的数据格式
    private Map<String,Object> resultData=new HashMap<String,Object>();
    /**
     * 登录
     * @param account--账号
     * @param password --密码
     * @return
     */
    @RequestMapping(value = "/login")
    @ResponseBody
    public Map<String,Object> login(@RequestParam String account,
                                    @RequestParam String password,
                                    HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        resultData.clear();
        resultData.put("code", 400);//失败
        resultData.put("info", "登录账号或密码错误！");
        resultData.put("data", "");
        try {
            DrivingAccount drivingAccount= (DrivingAccount) baseDao.load(DrivingAccount.class,"where account = '"+account+"' and password = '"+password+"'",false);
            if(null!=drivingAccount){
                resultData.put("code", 200);//成功
                resultData.put("info", "登录成功！");
                resultData.put("data", drivingAccount.getId());
            }
        } catch (Exception e) {
            log.error("登录异常："+e);
        }
        return resultData;
    }
}
