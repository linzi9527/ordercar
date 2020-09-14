package com.ordercar.controller;


import com.ordercar.utils.AllowOrigin;
import com.ordercar.utils.DateUtils;
import com.ordercar.utils.OrderUtils;
import com.ordercar.utils.SendNoticeUtil;
import com.ordercar.vo.*;
import com.summaryday.framework.d.IBaseDao;
import com.summaryday.framework.db.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 手机端约车相关
 */
@Slf4j
@Controller
@RequestMapping("/v1/wxCarHailing")
public class WxCarHailingController {


    @Autowired
    private IBaseDao baseDao;

    //接口数据封装类，返回给前端的数据格式
    private Map<String,Object> resultData=new HashMap<String,Object>();

    /**
     * 获取设置时间段配置信息
     * @param drivingId
     * @return
     */
    @RequestMapping(value = "/getTimeSet")
    @ResponseBody
    public Map<String,Object> getTimeSet(@RequestParam String drivingId, HttpServletResponse resp){
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
    // 首页约车，车辆列表
    @RequestMapping(value = "/listCarInfoVo")
    @ResponseBody
    public List<CarInfoVo> listCarInfoVo(@RequestParam String drivingId,@RequestParam String type, @RequestParam String time,HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        List<CarInfoVo> countList = new ArrayList<>();
        try {
            String sql = "select tbl_carinfo.id,tbl_carinfo.carimg,tbl_carinfo.carname,tbl_carinfo.cartype from tbl_carinfo where tbl_carinfo.status = '1' ";
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
            List<CarInfoVo> list = (List<CarInfoVo>) baseDao.queryTables(CarInfoVo.class,new String[] {"tbl_carinfo",},sql,false);
            countList = getCarInfoVoList(list,drivingId,time,type);//拼接历史预约量以及余量
        } catch (Exception e) {
            log.error("车辆信息列表异常："+e);
        }
        return countList;
    }
    //拼接历史预约量，以及余量
    private List<CarInfoVo> getCarInfoVoList(List<CarInfoVo> list,String drivingId,String time,String type) throws Exception {
        if(null==list){
            return new ArrayList<>();
        }
        for(int i = 0;i<list.size();i++){
            CarInfoVo carInfoVo = list.get(i);
            String sql = "SELECT * from tbl_order WHERE tbl_order.carinfoId = '"+carInfoVo.getId()+"' and tbl_order.status = '1'";
            List<Order> historyOrdersList= (List<Order>) baseDao.queryList(Order.class,sql,false);
            if(null!=historyOrdersList&&historyOrdersList.size()>0){
                carInfoVo.setHistoryNumber(historyOrdersList.size()+"");
            }else{
                carInfoVo.setHistoryNumber("0");
            }
            //计算已经下单的数量
            sql = "SELECT * FROM(SELECT tbl_time_slot.id,tbl_time_slot.startTime,tbl_time_slot.endTime,IFNULL(tbl_order.id, 0) AS reservedNumber,curtime() AS curtime," +
                    " tbl_time_slot.`status` FROM tbl_time_slot" +
                    " LEFT JOIN tbl_order ON tbl_time_slot.id = tbl_order.timeSlotId AND tbl_order.carinfoId = '"+carInfoVo.getId()+"' " +
                    " AND tbl_order.time = '"+time+"' AND tbl_order.`status` = '1' WHERE tbl_time_slot.drivingId = '"+drivingId+"' " +
                    " AND tbl_time_slot.type = '"+type+"' AND tbl_time_slot.`status` ='1' AND tbl_order.id IS NULL ORDER BY tbl_time_slot.startTime ASC) a " +
                    " WHERE CONCAT('"+time+"',' ',a.startTime) > DATE_FORMAT(now(), '%Y-%m-%d %H:%i')";
            List<RemainderDetailVo> times= (List<RemainderDetailVo>) baseDao.queryTables(RemainderDetailVo.class,new String[]{"tbl_time_slot","tbl_order"},sql,false);
            if(null!=times){
                carInfoVo.setSurplusNumber(times.size()+"");
            }else{
                carInfoVo.setSurplusNumber("0");
            }
        }
        return list;
    }
    // 拉取预约时间段信息列表
    @RequestMapping(value = "/getRemainderDetail")
    @ResponseBody
    public Map<String,Object> getRemainderDetail(@RequestParam String drivingId, @RequestParam String carinfoId,
                                                 @RequestParam String time,@RequestParam String type,
                                                 HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        resultData.clear();
        resultData.put("code", 400);//失败
        resultData.put("info", "操作失败！");
        List<RemainderDetailVo> times = new ArrayList<>();
        try { String sql = "SELECT * from (SELECT tbl_time_slot.id,tbl_time_slot.startTime,tbl_time_slot.endTime,IFNULL(tbl_order.id,0) AS reservedNumber,curtime() AS curtime,tbl_time_slot.status FROM tbl_time_slot " +
                "LEFT JOIN tbl_order ON tbl_time_slot.id = tbl_order.timeSlotId and tbl_order.carinfoId = '"+carinfoId+"' AND tbl_order.time = '"+time+"' and tbl_order.status='1' " +
                "WHERE tbl_time_slot.drivingId = '"+drivingId+"' AND tbl_time_slot.type = '"+type+"' ORDER BY tbl_time_slot.startTime ASC ) a";
            times= (List<RemainderDetailVo>) baseDao.queryTables(RemainderDetailVo.class,new String[]{"tbl_time_slot","tbl_order"},sql,false);
            resultData.put("code", 200);//成功
            resultData.put("info", "操作成功！");
            resultData.put("data", times);
        } catch (Exception e) {
            log.error("获取设置时间段配置信息异常："+e);
        }
        return resultData;
    }
    // 提交预约
    @RequestMapping(value = "/saveOrder")
    @ResponseBody
    public Map<String,Object> saveOrder(@RequestParam String drivingId,
           @RequestParam String carinfoId,@RequestParam String timeSlotIds,@RequestParam String type,
           @RequestParam String name,@RequestParam String tel,@RequestParam String openId,@RequestParam String timeSlots,
           @RequestParam String time,String remarks, HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        resultData.clear();
        resultData.put("code", 400);//失败
        resultData.put("info", "预约失败！");
        try {
            String [] timeSlotIdArr  = timeSlotIds.split(",");
            int number = 0;
            //校验时间段是否已经预约过
            TimeSet timeSet= (TimeSet) baseDao.load(TimeSet.class, "where drivingId = '"+drivingId+"'" , false);
            if(null!=timeSet){
                if("0".equals(timeSet.getIsNumber())){//不允许多个时间段
                    number = 1;
                }else{
                    number = Integer.parseInt(timeSet.getNumber());//允许多个时间段,获取允许几个时间段
                }
                if(timeSlotIdArr.length>number){
                    log.info("当前手机号码超出了限制次数：number:"+number+",cnt:"+timeSlotIdArr.length);
                    resultData.put("code", 400);//失败
                    resultData.put("info", "预约失败,同一天只允许预约"+number+"次时间段！");
                    return resultData;
                }
                //获取当前人，当前时间预约次数是否已经超出限制次数
                String sql = "SELECT * from tbl_order WHERE tbl_order.time = '"+time+"' and openId = '"+openId+"'";
                List<Order>  historyOrdersList= (List<Order>) baseDao.queryList(Order.class,sql,false);
                if(null!=historyOrdersList&&historyOrdersList.size()>0){
                  int cnt = timeSlotIdArr.length + historyOrdersList.size();//获取总数
                  if(cnt>number){
                      log.info("当前手机号码之前已经预约过了，总共超出了限制次数：number:"+number+",cnt:"+cnt);
                      resultData.put("code", 400);//失败
                      resultData.put("info", "预约失败,同一天只允许预约"+number+"次时间段！");
                      return resultData;
                  }
                }
            }else{
                resultData.put("code", 400);//失败
                resultData.put("info", "预约失败,基础信息未设置！");
                return resultData;
            }
            boolean flag = false;
            String orderId = OrderUtils.toCode(1);
            String createTime = DateUtil.getCurDateTime();
            String timeSlotArr[] = timeSlots.split(",");
            for(int i = 0;i<timeSlotIdArr.length;i++){
                String timeSlotId = timeSlotIdArr[i];
                Order order = new Order();
                order.setId(UUID.randomUUID().toString().replaceAll("-",""));
                order.setOrderId(orderId);//获取订单ID
                order.setStatus("1");
                order.setOpenId(openId);
                order.setTimeSlot(timeSlotArr[i]);
                order.setCreateTime(createTime);
                order.setCarinfoId(carinfoId);
                order.setDrivingId(drivingId);
                order.setTimeSlotId(timeSlotId);
                order.setType(type);
                order.setName(name);
                order.setTel(tel);
                order.setTime(time);
                order.setRemarks(remarks);
                flag = baseDao.save(order);
                if(!flag){
                    break;
                }
            }
            if(flag){
                //给学员发微信发送
                DrivingAccount drivingAccount= (DrivingAccount) baseDao.get(drivingId,DrivingAccount.class,false);//驾校信息
                CarInfo carInfo= (CarInfo) baseDao.get(carinfoId,CarInfo.class,false);// 车辆信息
                if(null!=drivingAccount && null!=carInfo){
                    Map<String, Object> infoMap = new HashMap<String, Object>();
                    infoMap.put("openId", openId);
                    infoMap.put("first","尊敬的用户，您有一条新的约车信息");
                    String typeName = type.equals("0")?"科目二":"科目三";
                    infoMap.put("keyword1", drivingAccount.getDrivingname()+" "+typeName+" "+carInfo.getCarname()+" "+carInfo.getCartype());
                    infoMap.put("keyword2", name);
                    infoMap.put("keyword3", tel);
                    infoMap.put("keyword4", time+" "+timeSlots);
                    infoMap.put("keyword5", drivingAccount.getAddress());
                    infoMap.put("remark", "如果您不能按时到达，请提前6小时取消预约！如违约，会对您下次约车造成影响！");
                    SendNoticeUtil.SendNotice(infoMap, "CHYUTX", "",drivingAccount.getAppid(),drivingAccount.getAppkey());
                }else{
                    log.info("获取驾校,车辆信息异常");
                }
                resultData.put("code", 200);//成功
                resultData.put("info", "预约成功！");
            }
        } catch (Exception e) {
            log.error("获取设置时间段配置信息异常："+e);
        }
        return resultData;
    }
    // 订单信息列表
    @RequestMapping(value = "/userListOrder")
    @ResponseBody
    public List<OrderVo> userListOrder(@RequestParam String openId, HttpServletResponse resp) {
        AllowOrigin.AllowOrigin(resp);
        log.info("openId:" + openId);
        List<OrderVo> list = new ArrayList<>();
        try {
            String sql = "SELECT tbl_order.id,tbl_order.orderId,tbl_order.`name`,tbl_order.tel,tbl_carinfo.carimg,tbl_carinfo.carname," +
                    " tbl_carinfo.cartype,GROUP_CONCAT(tbl_order.timeSlot) AS timeSlot,tbl_order.time,tbl_order.`status`,tbl_order.remarks,tbl_order.createTime FROM tbl_order " +
                    " LEFT JOIN tbl_carinfo ON tbl_order.carinfoId = tbl_carinfo.id" +
                    " where 1 = 1 ";
            if (null != openId && "" != openId) {
                sql = sql + " and tbl_order.openId = '" + openId + "'";
            }
            sql = sql + " GROUP BY tbl_order.orderId ORDER BY tbl_order.createTime DESC";
            list = (List<OrderVo>) baseDao.queryTables(OrderVo.class, new String[]{"tbl_order", "tbl_carinfo"}, sql, false);
        } catch (Exception e) {
            log.error("订单信息列表信息异常：" + e);
        }
        return list;
    }
    // 取消订单
    @RequestMapping(value = "/cancelOrder")
    @ResponseBody
    public Map<String,Object> cancelOrder(@RequestParam String orderId, HttpServletResponse resp) {
        AllowOrigin.AllowOrigin(resp);
        log.info("orderId:" + orderId);
        resultData.clear();
        resultData.put("code", 400);//失败
        resultData.put("info", "操作失败！");
        try {
            String sql = "SELECT * FROM tbl_order WHERE `orderId` = '"+orderId+"' ORDER BY tbl_order.timeSlot ASC";
            List<Order> list = (List<Order>) baseDao.queryTables(Order.class, new String[]{"tbl_order"}, sql, false);
            String time = "";
            if(null!=list && list.size()>0){
                Order order = list.get(0);
                String timeSlot = order.getTimeSlot().split("-")[0];
                time = order.getTime()+" "+timeSlot+":00";
                long t = 6*60*60;//6小时
                long t1 = DateUtils.getDistance(time, DateUtils.doConvertToString(new Date()));
                if(t1 > t && DateUtils.afterToSystemDate(time)){//预约时间在系统时间之后，6小时之外
                    String updateSql = "UPDATE `ordercar`.`tbl_order` SET  `status` = '0' WHERE `orderId` = '"+orderId+"'";
                    int cnt = baseDao.executeCUD(updateSql);
                    if(cnt>0){
                        resultData.put("code", 200);//成功
                        resultData.put("info", "操作成功！");
                    }
                }else{
                    resultData.put("code", 400);//成功
                    resultData.put("info", "操作失败,已经超过时效，无法取消预约！");
                }
            }
        } catch (Exception e) {
            log.error("获取设置时间段配置信息异常："+e);
        }
        return resultData;
    }
}
