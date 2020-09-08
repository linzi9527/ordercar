package com.ordercar.controller;

import com.ordercar.utils.AllowOrigin;
import com.ordercar.vo.CarInfo;
import com.ordercar.vo.CarParams;
import com.ordercar.vo.DrivingAccount;
import com.summaryday.framework.d.IBaseDao;
import com.summaryday.framework.db.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车辆管理维护
 *
 */
@Slf4j
@Controller
@RequestMapping("/v1")
public class CarManagerController {

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
    //@RequestMapping(value = "/addCarInfo")
    @RequestMapping(value = "/addCarInfo",method = RequestMethod.POST,consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> CarInfo(CarInfo carInfo, HttpServletResponse resp){
       // public Map<String,Object> CarInfo(@RequestBody CarInfo carInfo, HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        log.info("carInfo:"+carInfo);
        resultData.clear();
        resultData.put("code", 400);//失败
        resultData.put("info", "操作失败！");
        if(carInfo!=null){
            try {
                baseDao.save(carInfo);
                resultData.put("code", 200);//成功
                resultData.put("info", "操作成功！");
            } catch (Exception e) {
                log.error("新增驾校账户信息异常："+e);
            }
        }
        return resultData;
    }


    /**
     * 车辆维护列表
     * @param km 科目二、科目三
     * @param isOrderNo 是否排序:1使用排序，0不使用
     * @param searchKey 搜素关键词
     * @param carType   车辆类型C1、C2
     * @param startIndex 起始下标
     * @param count 每页记录数
     * @param resp
     * @return
     */
    //@RequestMapping(value = "/listCar")
    @RequestMapping(value = "/listCar",method = RequestMethod.POST,consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> listCar(CarParams carParams, HttpServletResponse resp){
        //public Map<String,Object> listCar(@RequestBody CarParams carParams, HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        log.info("searchKey:"+carParams.getSearchKey()+",carType:"+carParams.getCarType());
        resultData.clear();
        resultData.put("code", 400);//失败
        resultData.put("info", "操作失败！");
        String SQL="",limit="",orderby="";
        if(carParams.getStartIndex()>=0&&carParams.getCount()>0){limit="  limit "+carParams.getStartIndex()+","+carParams.getCount();}
        if(StringUtil.isEmpty(carParams.getIsOrderNo())){
            orderby=" and status='1' order by createtime desc";
        }else{
            orderby=" and status='0' order by orderno ";
        }
        if(StringUtil.isEmpty(carParams.getSearchKey())){
            SQL=" where 1=1 and km='"+carParams.getKm()+"' "+orderby;
        }else{
            SQL=" where  km='"+carParams.getKm()+"' and carname like '%"+carParams.getSearchKey()+"%' and cartype='"+carParams.getCarType()+"' "+orderby;
        }
        try {
            List<CarInfo> list= (List<CarInfo>) baseDao.queryList(CarInfo.class,SQL+limit,false);
            resultData.put("code", 200);//成功
            resultData.put("info", "操作成功！");
            resultData.put("list", list);
            resultData.put("datacount", (list!=null&&list.size()>0)?list.size():0);
        } catch (Exception e) {
            log.error("驾校账户列表信息异常："+e);
        }
        return resultData;
    }

    //根据id查看车辆基本信息
    @RequestMapping(value = "/getCarInfo")
    @ResponseBody
    public Map<String,Object> getCarInfo(String id, HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        log.info("根据id查看车辆基本信息id:"+id);
        resultData.clear();
        resultData.put("code", 400);//失败
        resultData.put("info", "操作失败！");
        if(!StringUtil.isEmpty(id)){
            try {
                CarInfo carInfo= (CarInfo) baseDao.get(id,CarInfo.class,false);
                resultData.put("code", 200);//成功
                resultData.put("info", "操作成功！");
                resultData.put("CarInfo", carInfo);
            } catch (Exception e) {
                log.error("根据id查看车辆基本信息异常："+e);
            }
        }
        return resultData;
    }


    //根据id更新车辆基本信息
    //@RequestMapping(value = "/updateCarInfo")
    @RequestMapping(value = "/updateCarInfo",method = RequestMethod.POST,consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> updateCarInfo(CarInfo carInfo, HttpServletResponse resp){
       // public Map<String,Object> updateCarInfo(@RequestBody CarInfo carInfo, HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        log.info("根据id查看车辆基本信息carInfo:"+carInfo);
        resultData.clear();
        resultData.put("code", 400);//失败
        resultData.put("info", "操作失败！");
        if(null!=carInfo){
            try {
                baseDao.update(carInfo);
                resultData.put("code", 200);//成功
                resultData.put("info", "操作成功！");
            } catch (Exception e) {
                log.error("根据id更新车辆基本信息异常："+e);
            }
        }
        return resultData;
    }


    //根据id停用车辆
    @RequestMapping(value = "/stopCarInfo")
    @ResponseBody
    public Map<String,Object> stopCarInfo(String id, HttpServletResponse resp){
        AllowOrigin.AllowOrigin(resp);
        log.info("根据id停用车辆id:"+id);
        resultData.clear();
        resultData.put("code", 400);//失败
        resultData.put("info", "操作失败！");
        if(!StringUtil.isEmpty(id)){
            try {
                CarInfo carInfo= (CarInfo) baseDao.get(id,CarInfo.class,false);
                carInfo.setStatus("0");//停用
                baseDao.update(carInfo);
                resultData.put("code", 200);//成功
                resultData.put("info", "操作成功！");
            } catch (Exception e) {
                log.error("根据id停用车辆异常："+e);
            }
        }
        return resultData;
    }



}
