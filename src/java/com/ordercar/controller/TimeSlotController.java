package com.ordercar.controller;


import com.ordercar.utils.AllowOrigin;
import com.ordercar.vo.Page;
import com.ordercar.vo.TimeSlot;
import com.summaryday.framework.d.IBaseDao;
import com.summaryday.framework.db.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 时间段信息表
 */
@Slf4j
@Controller
@RequestMapping("/v1")
public class TimeSlotController {

    @Autowired
    private IBaseDao baseDao;

    //接口数据封装类，返回给前端的数据格式
    private Map<String,Object> resultData=new HashMap<String,Object>();

    // 时间段信息列表
    @RequestMapping(value = "/listTimeSlot")
    @ResponseBody
    public Page<TimeSlot> listTimeSlot(@RequestParam int pageNow, @RequestParam int pageCount,@RequestParam String drivingId,
                                       @RequestParam String type,@RequestParam String status, HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        log.info("pageNow:"+pageNow+",pageCount:"+pageCount);
        Page<TimeSlot> page = new Page<TimeSlot>();
        try {
            page.setPageNow(pageNow);
            page.setRowCount(pageCount);
            int strat=(page.getPageNow()-1)*page.getPageSize();
            String sql = "select * from tbl_time_slot where 1 = 1 ";
            if(null!=status && "" != status){
                sql = sql +" and tbl_time_slot.status = '"+status+"'";
            }
            if(null!=type && "" != type){
                sql = sql +" and tbl_time_slot.type = '"+type+"'";
            }
            if(null!=drivingId && "" != drivingId){
                sql = sql +" and tbl_time_slot.drivingId = '"+drivingId+"'";
            }
            sql = sql +" ORDER BY tbl_time_slot.createTime DESC";
            List<TimeSlot> list= (List<TimeSlot>) baseDao.queryList(TimeSlot.class,sql+" limit "+ strat +","+ page.getPageSize(),false);
            page.setList(list);
            List<TimeSlot> countList= (List<TimeSlot>) baseDao.queryList(TimeSlot.class,sql,false);
            //总记录数
            if(null!=countList){
                page.setRowCount(list.size());
            }else{
                page.setRowCount(0);
            }
        } catch (Exception e) {
            log.error("时间段信息表列表信息异常："+e);
        }
        return page;
    }

    /**ih
     * 新增时间段
     * @param timeSlot
     * @param resp
     * @return
     */
    @RequestMapping(value = "/addTimeSlot",method = RequestMethod.POST,consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> addTimeSlot(TimeSlot timeSlot, HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        log.info("timeSlot:"+timeSlot);
        resultData.clear();
        resultData.put("code", 400);//失败
        resultData.put("info", "操作失败！");
        if(timeSlot!=null){
            try {
                timeSlot.setId(UUID.randomUUID().toString().replaceAll("-",""));
                timeSlot.setStatus("1");
                timeSlot.setCreateTime(DateUtil.getCurDateTime());
                String sql = "select * from tbl_time_slot where (startTime > '"+timeSlot.getStartTime()+"' AND startTime < '"+timeSlot.getEndTime()+"')" +
                        " OR (startTime < '"+timeSlot.getStartTime()+"' AND endTime > '"+timeSlot.getEndTime()+"') " +
                        " OR (endTime > '"+timeSlot.getStartTime()+"' AND endTime < '"+timeSlot.getEndTime()+"')";
                List<TimeSlot> countList= (List<TimeSlot>) baseDao.queryList(TimeSlot.class,sql,false);
                if(null!=countList&&countList.size()>0){
                    resultData.put("code", 400);//失败
                    resultData.put("info", "操作失败,存在交集时间段！");
                }else{
                    boolean flag = baseDao.save(timeSlot);
                    if(flag){
                        resultData.put("code", 200);//成功
                        resultData.put("info", "操作成功！");
                    }
                }
            } catch (Exception e) {
                log.error("新增时间段信息异常："+e);
            }
        }
        return resultData;
    }
    /**
     * 更新时间段
     * @param timeSlot
     * @param resp
     * @return
     */
    @RequestMapping(value = "/updateTimeSlot",method = RequestMethod.POST,consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> updateTimeSlot(TimeSlot timeSlot, HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        log.info("timeSlot:"+timeSlot);
        resultData.clear();
        resultData.put("code", 400);//失败
        resultData.put("info", "操作失败！");
        if(timeSlot!=null){
            try {
                String sql = "select * from tbl_time_slot where (startTime > '"+timeSlot.getStartTime()+"' AND startTime < '"+timeSlot.getEndTime()+"')" +
                        " OR (startTime < '"+timeSlot.getStartTime()+"' AND endTime > '"+timeSlot.getEndTime()+"') " +
                        " OR (endTime > '"+timeSlot.getStartTime()+"' AND endTime < '"+timeSlot.getEndTime()+"') and id !='"+timeSlot.getId()+"'";
                List<TimeSlot> countList= (List<TimeSlot>) baseDao.queryList(TimeSlot.class,sql,false);
                if(null!=countList&&countList.size()>0){
                    resultData.put("code", 400);//失败
                    resultData.put("info", "操作失败,存在交集时间段！");
                }else{
                    boolean flag = baseDao.update(timeSlot);
                    if(flag){
                        resultData.put("code", 200);//成功
                        resultData.put("info", "操作成功！");
                    }
                }
            } catch (Exception e) {
                log.error("更新时间段信息异常："+e);
            }
        }
        return resultData;
    }
    /**
     * 启用、停用时间段:1/0
     * @param id
     * @param status
     * @return
     */
    @RequestMapping(value = "/openTimeSlot",method = RequestMethod.POST,consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> openTimeSlot(@RequestParam String id,@RequestParam String status, HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        log.info("id:"+id+"==status:"+status);
        resultData.clear();
        resultData.put("code", 400);//失败
        resultData.put("info", "操作失败！");
        if(id!=null&&status!=null){
            try {
                String openTime ="";
                if("1".equals(status)){
                     openTime = DateUtil.getCurDate();// 启用时间
                }
                String updateSql = "UPDATE `ordercar`.`tbl_time_slot` SET  `status`='"+status+"', `openTime`='"+openTime+"' WHERE `id`='"+id+"'";
                int cnt = baseDao.executeCUD(updateSql);
                if(cnt>0){
                    resultData.put("code", 200);//成功
                    resultData.put("info", "操作成功！");
                }
            } catch (Exception e) {
                log.error(" 启用、停用时间段信息异常："+e);
            }
        }
        return resultData;
    }
    /**
     * 批量启用、批量停用时间段:1/0
     * @param ids
     * @param status
     * @return
     */
    @RequestMapping(value = "/batchopenTimeSlot",method = RequestMethod.POST,consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> batchopenTimeSlot(@RequestParam String ids,@RequestParam String status, HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        log.info("ids:"+ids+"==status:"+status);
        resultData.clear();
        resultData.put("code", 400);//失败
        resultData.put("info", "操作失败！");
        if(ids!=null&&status!=null){
            try {
                String str[] = ids.split(",");
                String openTime ="";
                if("1".equals(status)){
                    openTime = DateUtil.getCurDate();// 启用时间
                }
                for(int i = 0;i<str.length;i++){
                    String updateSql = "UPDATE `ordercar`.`tbl_time_slot` SET  `status`='"+status+"', `openTime`='"+openTime+"' WHERE `id`='"+str[i]+"'";
                    baseDao.executeCUD(updateSql);
                }
                resultData.put("code", 200);//成功
                resultData.put("info", "操作成功！");
            } catch (Exception e) {
                log.error("批量启用、批量停用时间段信息异常："+e);
            }
        }
        return resultData;
    }
}
