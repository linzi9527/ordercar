package com.ordercar.controller;


import com.ordercar.utils.AllowOrigin;
import com.ordercar.vo.CarInfo;
import com.ordercar.vo.OrderVo;
import com.ordercar.vo.Page;
import com.ordercar.vo.TimeSlot;
import com.summaryday.framework.d.IBaseDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单列表
 */
@Slf4j
@Controller
@RequestMapping("/v1")
public class OrderController {

    @Autowired
    private IBaseDao baseDao;

    //接口数据封装类，返回给前端的数据格式
    private Map<String,Object> resultData=new HashMap<String,Object>();

    // 订单信息列表
    @RequestMapping(value = "/listOrder")
    @ResponseBody
    public Page<OrderVo> listOrder(@RequestParam int pageNow,@RequestParam int pageCount,@RequestParam String drivingId,
                                   @RequestParam String type,String keyword,String status,String carinfoId,String timeSlotId,String startTime,String endTime,
                                   HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        log.info("pageNow:"+pageNow+",pageCount:"+pageCount);
        Page<OrderVo> page = new Page<OrderVo>();
        try {
            page.setPageNow(pageNow);
            page.setRowCount(pageCount);
            int strat=(page.getPageNow()-1)*page.getPageSize();
            String sql = "SELECT tbl_order.id,tbl_order.orderId,tbl_order.`name`,tbl_order.tel,tbl_carinfo.carimg,tbl_carinfo.carname," +
                    " tbl_carinfo.cartype,tbl_time_slot.startTime,tbl_time_slot.endTime,tbl_order.time,tbl_order.`status`,tbl_order.remarks FROM tbl_order " +
                    " LEFT JOIN tbl_carinfo ON tbl_order.carinfoId = tbl_carinfo.id" +
                    " LEFT JOIN tbl_time_slot ON tbl_order.timeSlotId = tbl_time_slot.id where 1 = 1 ";
            if(null!=type && "" != type){
                sql = sql +" and tbl_order.type = '"+type+"'";
            }
            if(null!=drivingId && "" != drivingId){
                sql = sql +" and tbl_order.drivingId = '"+drivingId+"'";
            }
            if(null!=keyword && "" != keyword){
                sql = sql +" and (tbl_order.tel like '%"+keyword+"%' or tbl_order.`name` like '%"+keyword+"%' or tbl_order.orderId like '%"+keyword+"%')";
            }
            if(null!=status && "" != status){
                sql = sql +" and tbl_order.status = '"+status+"'";//状态:0已取消，1已预约
            }
            if(null!=carinfoId && "" != carinfoId){
                sql = sql +" and tbl_order.carinfoId = '"+carinfoId+"'";//车辆ID
            }
            if(null!=timeSlotId && "" != timeSlotId){
                sql = sql +" and tbl_order.timeSlotId = '"+timeSlotId+"'";// 预约时段ID
            }
            if(null != startTime && "" != startTime){
                sql= sql +" and tbl_order.time >= '"+startTime+"'";
            }
            if(null != endTime && "" != endTime){
                sql= sql + " and tbl_order.time <= '"+endTime+"'";
            }
            sql = sql +" ORDER BY tbl_order.createTime DESC";
            List<OrderVo> list= (List<OrderVo>) baseDao.queryTables(OrderVo.class,new String[] {"tbl_order","tbl_carinfo","tbl_time_slot"},sql+" limit "+ strat +","+ page.getPageSize(),false);
            page.setList(list);
            List<OrderVo> countList= (List<OrderVo>) baseDao.queryTables(OrderVo.class,new String[] {"tbl_order","tbl_carinfo","tbl_time_slot"},sql,false);
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
    // 时间段信息列表
    @RequestMapping(value = "/listTimeSlotOrder")
    @ResponseBody
    public List<TimeSlot> listTimeSlotOrder(@RequestParam String drivingId,@RequestParam String type,HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        List<TimeSlot> countList = new ArrayList<>();
        try {
            String sql = "select * from tbl_time_slot where 1 = 1 ";
            if(null!=drivingId && "" != drivingId){
                sql = sql +" and tbl_time_slot.drivingId = '"+drivingId+"'";
            }
            if(null!=type && "" != type){
                sql = sql +" and tbl_time_slot.type = '"+type+"'";
            }
            sql = sql +" ORDER BY tbl_time_slot.startTime asc";
            countList= (List<TimeSlot>) baseDao.queryList(TimeSlot.class,sql,false);
        } catch (Exception e) {
            log.error("时间段信息表列表信息异常："+e);
        }
        return countList;
    }
    // 时间段信息列表
    @RequestMapping(value = "/listCarInfoOrder")
    @ResponseBody
    public List<CarInfo> listCarInfoOrder(@RequestParam String drivingId,@RequestParam String type,HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        List<CarInfo> countList = new ArrayList<>();
        try {
            String sql = "select * from tbl_carinfo where 1 = 1 ";
            if(null!=drivingId && "" != drivingId){
                sql = sql +" and tbl_carinfo.belong = '"+drivingId+"'";
            }
            if(null!=type && "" != type){
                if("1".equals(type)){
                    sql = sql +" and tbl_carinfo.km = '科目三'";
                }else{
                    sql = sql +" and tbl_carinfo.km = '科目二'";
                }
            }
            sql = sql +" ORDER BY tbl_carinfo.orderno asc";
            countList= (List<CarInfo>) baseDao.queryList(CarInfo.class,sql,false);
        } catch (Exception e) {
            log.error("时间段信息表列表信息异常："+e);
        }
        return countList;
    }
}