package com.ordercar.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @ClassName:FileUploadController
 * @Description:TODO
 * @Author:nano
 * @Version:1.0
 */
@Slf4j
@Controller
@RequestMapping("/v1")
public class FileUploadController {

    private Map<String,Object> resultData=new HashMap<String,Object>();

    @ResponseBody
    @RequestMapping("/upload")
    public Map<String,Object> uploadPicture(@RequestParam(value="file",required=false) MultipartFile file, HttpServletRequest request){
//    public Map<String,Object> uploadPicture(@RequestParam(value="file",required=false) MultipartFile file, HttpServletRequest request){
        resultData.put("code", 400);//失败
        resultData.put("info", "操作失败！");
        File targetFile=null;
        String url="";//返回存储路径
        log.info("上传文件:"+file);
        String fileName=file.getOriginalFilename();//获取文件名加后缀
        if(fileName!=null&&fileName!=""){
            //String returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() +"/upload/img/";//存储路径
            //String returnUrl =request.getContextPath() +"/upload/car/";//存储路径
            String returnUrl ="./upload/car/";//存储路径
            String path = request.getSession().getServletContext().getRealPath(returnUrl); //文件存储位置
            String fileF = fileName.substring(fileName.lastIndexOf("."), fileName.length());//文件后缀
            fileName=new Date().getTime()+"_"+new Random().nextInt(1000)+fileF;//新的文件名

            //先判断文件是否存在
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String fileAdd = sdf.format(new Date());
            //获取文件夹路径
            File file1 =new File(path+"/"+fileAdd);
            //如果文件夹不存在则创建
            if(!file1 .exists()  && !file1 .isDirectory()){
                file1 .mkdir();
            }
            //将图片存入文件夹
            targetFile = new File(file1, fileName);
            try {
                //将上传的文件写到服务器上指定的文件。
                file.transferTo(targetFile);
                url=returnUrl+fileAdd+"/"+fileName;
                log.info("图片地址Url:"+url);
                resultData.put("code", 200);//成功
                resultData.put("info", "图片上传成功！");
                resultData.put("flieurl", url);
                resultData.put("url", fileAdd+"/"+fileName);
            } catch (Exception e) {
                resultData.put("info", "系统异常，图片上传失败！");
                log.error("系统异常，图片上传失败:"+e);
            }
        }
        return resultData;
    }




}
