package com.ordercar.utils;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
/**
 * 微信模板推送公用方法
 * @author zlzhaoe
 */
@Slf4j
public class SendNoticeUtil {
	
	public static final String CHYUTX = "CHYUTX";//客户预约车辆信息提醒

	public static boolean SendNotice(Map<String, Object> info,String type,String url,String APPID,String APPSECRET) {
		boolean result = false;
		String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
		requestUrl = requestUrl.replace("APPID",APPID).replace("APPSECRET",APPSECRET);
		JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
		System.out.println(jsonObject);
		if(null != jsonObject && !jsonObject.isEmpty()){
			String access_token = jsonObject.getString("access_token");
			if(StringUtils.isNotEmpty(access_token)){
				String requestUrl1 = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
				requestUrl1 = requestUrl1.replace("ACCESS_TOKEN", access_token);
				if(StringUtils.isNotEmpty(type)){
					JSONObject wxReturnObject = null;
					if(CHYUTX.equals(type)){
						wxReturnObject = CommonUtil.httpsRequest(requestUrl1, "GET", recordPushSendOffTemplate(info, url).toString());
						System.out.println(wxReturnObject);
						log.info("微信模板推送公用方法："+wxReturnObject);
					}
				}
			}
		}
		return result;
	}
	/**
	 * 客户预约车辆信息提醒推送
	 * @param info
	 * @param url
	 * @return
	 */
	private static Object recordPushSendOffTemplate(Map<String, Object> info,String url) {
	       JSONObject jsonObject = new JSONObject();
	        jsonObject.put("touser", info.get("openId"));  
	        jsonObject.put("template_id", "4LBhSBKBafvILLQJyjpuxGGJngAVSPt_4l6H24dmR3I"); //模板ID
	        if(StringUtils.isNotEmpty(url)){
	        	jsonObject.put("url", url); //详情页面url是可选参数，不进行跳链
	        }
	        jsonObject.put("topcolor", "#FF0000"); 
	        JSONObject first=new JSONObject();
	        first.put("value", info.get("first"));
	        first.put("color", "#173177");
	        JSONObject keyword1=new JSONObject();
	        keyword1.put("value", info.get("keyword1"));
	        keyword1.put("color", "#173177");
	        JSONObject keyword2=new JSONObject();
	        keyword2.put("value", info.get("keyword2"));
	        keyword2.put("color", "#173177");
	        JSONObject keyword3=new JSONObject();
	        keyword3.put("value", info.get("keyword3"));
	        keyword3.put("color", "#173177");
			JSONObject keyword4=new JSONObject();
			keyword4.put("value", info.get("keyword4"));
			keyword4.put("color", "#173177");
			JSONObject keyword5=new JSONObject();
			keyword5.put("value", info.get("keyword5"));
			keyword5.put("color", "#173177");
	        JSONObject remark=new JSONObject();
	        remark.put("value", info.get("remark"));
	        remark.put("color", "#173177");
	        JSONObject data=new JSONObject();  
	        data.put("first", first);  
	        data.put("keyword1", keyword1);  
	        data.put("keyword2", keyword2);  
	        data.put("keyword3", keyword3);
			data.put("keyword4", keyword4);
			data.put("keyword5", keyword5);
			data.put("remark", remark);
	        jsonObject.element("data", data);  
	        return jsonObject;
	}
	public static void main(String args[]){
		//给学员发微信发送
		Map<String, Object> infoMap = new HashMap<String, Object>();
		infoMap.put("openId", "oEYBst0CpjZriQuusBRJ63rxHHbs");
		infoMap.put("first","尊敬的用户，您有一条新的约车信息");
		infoMap.put("keyword1", "白鹿驾校科二 2号车 C1");
		infoMap.put("keyword2", "张三");
		infoMap.put("keyword3", "18192517977");
		infoMap.put("keyword4", "2014年10月16日  7：30-9：30");
		infoMap.put("keyword5", "西安科技路50号");
		infoMap.put("remark", "如果您不能按时到达，请提前6小时取消预约！如违约，会对您下次约车造成影响！");
		SendNoticeUtil.SendNotice(infoMap, "CHYUTX", "","wx9766e4b2a8cb0844","53a60d01dace6a93fdb829f9d8fbbc7d");
	}
}
  
