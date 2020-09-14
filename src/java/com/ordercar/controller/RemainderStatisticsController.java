package com.ordercar.controller;


import com.ordercar.utils.AllowOrigin;
import com.ordercar.utils.DateUtils;
import com.ordercar.vo.*;
import com.summaryday.framework.d.IBaseDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 余号统计
 */
@Slf4j
@Controller
@RequestMapping("/v1")
public class RemainderStatisticsController {

    @Autowired
    private IBaseDao baseDao;

    //接口数据封装类，返回给前端的数据格式
    private Map<String,Object> resultData=new HashMap<String,Object>();

    // 余号统计信息列表
    @RequestMapping(value = "/listRemainder")
    @ResponseBody
    public Page<RemainderVo> listRemainder(@RequestParam int pageNow,@RequestParam int pageCount,
                                           @RequestParam String drivingId,@RequestParam String type,
                                           @RequestParam String time,HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        log.info("pageNow:"+pageNow+",pageCount:"+pageCount);
        Page<RemainderVo> page = new Page<RemainderVo>();
        try {
            page.setPageNow(pageNow);
            page.setRowCount(pageCount);
            int strat=(page.getPageNow()-1)*page.getPageSize();
            StringBuffer sqlb = new StringBuffer("SELECT tbl_carinfo.id,tbl_carinfo.carname,tbl_carinfo.status FROM tbl_carinfo WHERE 1 = 1");
            if(null!=type && "" != type){
                if("1".equals(type)){
                    sqlb.append(" and tbl_carinfo.km = '科目三'");
                }else{
                    sqlb.append(" and tbl_carinfo.km = '科目二'");
                }
            }
            if(null!=drivingId && "" != drivingId){
                sqlb.append(" and tbl_carinfo.belong = '"+drivingId+"'");
            }
            sqlb.append(" ORDER BY tbl_carinfo.orderno ASC");
            List<RemainderVo> list= (List<RemainderVo>) baseDao.queryTables(RemainderVo.class,new String[]{"tbl_carinfo"},sqlb+" limit "+ strat +","+ page.getPageSize(),false);
            page.setList(getDataList(list,time,drivingId,type));
            List<RemainderVo> countList= (List<RemainderVo>) baseDao.queryTables(RemainderVo.class,new String[]{"tbl_carinfo"},sqlb.toString(),false);
            //总记录数
            if(null!=countList){
                page.setRowCount(list.size());
            }else{
                page.setRowCount(0);
            }
        } catch (Exception e) {
            log.error("余号统计信息列表信息异常："+e);
        }
        return page;
    }
    //获取已预约车辆数量
    private List<RemainderVo> getDataList(List<RemainderVo> list,String time,String drivingId,String type) throws Exception {
        List<RemainderVo> result = new ArrayList<>();
        int allNumber = 0;
        String sql = "SELECT * from tbl_time_slot WHERE tbl_time_slot.drivingId = '"+drivingId+"' and tbl_time_slot.type = '"+type+"'  AND tbl_time_slot.`status` = '1'";
        List<TimeSlot> countList= (List<TimeSlot>) baseDao.queryList(TimeSlot.class,sql,false);
        if(null!=countList&&countList.size()>0){
            allNumber = countList.size();
        }
        for(int i = 0;i<list.size();i++){
            RemainderVo remainderVo = list.get(i);
            remainderVo.setAllNumber(allNumber+"");
            sql = "SELECT * from tbl_order WHERE tbl_order.carinfoId = '"+remainderVo.getId()+"' AND tbl_order.`status` = '1' AND tbl_order.time = '"+time+"'";
            List<Order> ordersList= (List<Order>) baseDao.queryList(Order.class,sql,false);
            if(null!=ordersList&&ordersList.size()>0){
                remainderVo.setReservedNumber(ordersList.size()+"");
                int surplusNumber = allNumber-ordersList.size();
                remainderVo.setSurplusNumber(surplusNumber+"");
                result.add(remainderVo);
            }else{
                if("1".equals(remainderVo.getStatus())){
                    remainderVo.setReservedNumber("0");
                    remainderVo.setSurplusNumber(allNumber+"");
                    result.add(remainderVo);
                }
            }
        }
        return result;
    }
    /**
     * 获取设置时间段配置信息
     * @param drivingId
     * @return
     */
    @RequestMapping(value = "/getListRemainderTime")
    @ResponseBody
    public Map<String,Object> getListRemainderTime(@RequestParam String drivingId, HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        resultData.clear();
        resultData.put("code", 400);//失败
        resultData.put("info", "操作失败！");
        List<String> times = new ArrayList<>();
        try {
            TimeSet timeSet= (TimeSet) baseDao.load(TimeSet.class, "where drivingId = '"+drivingId+"'", false);
            if(null!=timeSet){
                int time = Integer.parseInt(timeSet.getTime());//获取显示多少档
                times = DateUtils.findDates(DateUtils.convertToString(new Date()),DateUtils.getPastDate(time));
            }
            resultData.put("code", 200);//成功
            resultData.put("info", "操作成功！");
            resultData.put("data", times);
        } catch (Exception e) {
            log.error("获取设置时间段配置信息异常："+e);
        }
        return resultData;
    }
    /**
     * 获取余号统计信息列表详情
     * @param carinfoId
     * @param drivingId
     * @return
     */
    @RequestMapping(value = "/getRemainderDetail")
    @ResponseBody
    public Map<String,Object> getRemainderDetail(@RequestParam String drivingId,@RequestParam String carinfoId,
                                                 @RequestParam String time, @RequestParam String type,HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        resultData.clear();
        resultData.put("code", 400);//失败
        resultData.put("info", "操作失败！");
        List<RemainderDetailVo> times = new ArrayList<>();
        try { String sql = "SELECT * from (SELECT tbl_time_slot.id,tbl_time_slot.startTime,tbl_time_slot.endTime,IFNULL(tbl_order.id,0) AS reservedNumber,curtime() AS curtime,tbl_time_slot.status FROM tbl_time_slot " +
                "LEFT JOIN tbl_order ON tbl_time_slot.id = tbl_order.timeSlotId and tbl_order.carinfoId = '"+carinfoId+"' AND tbl_order.time = '"+time+"' AND tbl_order.`status` = '1' " +
                "WHERE tbl_time_slot.drivingId = '"+drivingId+"' AND tbl_time_slot.type = '"+type+"'  ORDER BY tbl_time_slot.startTime ASC ) a WHERE a.`status` = '1' or a.reservedNumber !='0'";
                times= (List<RemainderDetailVo>) baseDao.queryTables(RemainderDetailVo.class,new String[]{"tbl_time_slot","tbl_order"},sql,false);
                resultData.put("code", 200);//成功
                resultData.put("info", "操作成功！");
                resultData.put("data", times);
        } catch (Exception e) {
            log.error("获取设置时间段配置信息异常："+e);
        }
        return resultData;
    }
}
