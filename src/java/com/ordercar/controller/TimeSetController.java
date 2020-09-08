package com.ordercar.controller;


import com.ordercar.utils.AllowOrigin;
import com.ordercar.vo.TimeSet;
import com.summaryday.framework.d.IBaseDao;
import com.summaryday.framework.db.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/v1")
public class TimeSetController {

    @Autowired
    private IBaseDao baseDao;

    //接口数据封装类，返回给前端的数据格式
    private Map<String,Object> resultData=new HashMap<String,Object>();

    /**
     * 设置时间段信息
     * @param timeSet
     * @param resp
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateTimeSet",method = RequestMethod.POST,consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> saveOrUpdateTimeSet(TimeSet timeSet, HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        log.info("timeSet:"+timeSet);
        resultData.clear();
        resultData.put("code", 400);//失败
        resultData.put("info", "操作失败！");
        if(timeSet!=null){
            try {
                //删除原有配置
                String delSql = "DELETE from tbl_time_set where drivingId = '"+timeSet.getDrivingId()+"'";
                baseDao.executeCUD(delSql);
                timeSet.setId(UUID.randomUUID().toString().replaceAll("-",""));
                timeSet.setCreateTime(DateUtil.getCurDateTime());
                boolean flag = baseDao.save(timeSet);
                if(flag){
                    resultData.put("code", 200);//成功
                    resultData.put("info", "操作成功！");
                }
            } catch (Exception e) {
                log.error("设置时间段信息异常："+e);
            }
        }
        return resultData;
    }

    /**
     * 获取设置时间段配置信息
     * @param drivingId
     * @return
     */
    @RequestMapping(value = "/getTimeSet",method = RequestMethod.POST,consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> getTimeSet(String drivingId, HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        resultData.clear();
        resultData.put("code", 400);//失败
        resultData.put("info", "操作失败！");
        try {
            TimeSet timeSet= (TimeSet) baseDao.load(TimeSet.class, "where drivingId = '"+drivingId+"'", false);
            resultData.put("code", 200);//成功
            resultData.put("info", "操作成功！");
            resultData.put("data", timeSet);
        } catch (Exception e) {
            log.error("获取设置时间段配置信息异常："+e);
        }
        return resultData;
    }
}
