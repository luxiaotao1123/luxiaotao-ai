package com.wuai.company.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;  
import org.json.JSONObject; 

public class AliyunUtil {
   
	public static Map AliyunImageOrc(String picPath){
		    Map map =new HashMap();
		    String host = "https://dm-51.data.aliyun.com";
		    String path = "/rest/160601/ocr/ocr_idcard.json";
		    String method = "POST";
		    String appcode = "33d7bda16b6f465da90e598a33780602";
		    Map<String, String> headers = new HashMap<String, String>();
		    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
		    headers.put("Authorization", "APPCODE " + appcode);
		    //根据API的要求，定义相对应的Content-Type
		    headers.put("Content-Type", "application/json; charset=UTF-8");
		    Map<String, String> querys = new HashMap<String, String>();
		   
		    //String pathImage = "C:/Users/z/Desktop/positive.jpg"; 
		    String pathImage = "C:/Users/z/Desktop/positive.jpg"; 
	        String binaryToString = HttpUtils.getImageBinaryToString(pathImage);  
			String p = "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";
		   
	        String bodys = "{\"inputs\": [{\"image\": {\"dataType\": 50,\"dataValue\": \"" + p  
	                + "\"},\"configure\": {\"dataType\": 50,\"dataValue\": \"{\\\"side\\\":\\\"face\\\"}\"}}]}";  
	  
		    try {
		    	
		    	HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
		    	System.out.println(response.toString());
		    	//获取response的body
//		    	String res =  EntityUtils.toString(response.getEntity());
//		    	JSONObject resultObj =  JSONObject.fromObject(res); 
		    	JSONObject resultObj = new JSONObject(EntityUtils.toString(response.getEntity()));  
	            JSONArray outputArray = resultObj.getJSONArray("outputs");  
	            String output = outputArray.getJSONObject(0).getJSONObject("outputValue").getString("dataValue"); // 取出结果json字符串  
	            JSONObject out = new JSONObject(output);  
	            
	            if (out.getBoolean("success")) {  
	                String addr = out.getString("address"); // 获取地址  
	                map.put("address", addr);
	                String name = out.getString("name"); // 获取名字  
	                map.put("name", name);
	                String birth = out.getString("birth"); // 获取名字  
	                map.put("birth", birth);
	                String sex = out.getString("sex"); // 获取性别  
	                map.put("sex", sex);
	                String nationality = out.getString("nationality"); // 获取性别  
	                map.put("nationality", nationality);
	                String num = out.getString("num"); // 获取身份证号  
	                map.put("num", num);
	                System.out.printf(" name : %s \n sex : %s \n birth : %s \n nationality : %s \n num : %s\n address : %s\n", name, sex,birth,  
	                        nationality, num, addr);  
	            } else {  
	                System.out.println("predict error");  
	            }  
		    	 
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
			return map;
	}
	
	
	public static Map AliyunImageOrcrev(String picPath){
	    Map map =new HashMap();
	    String host = "https://dm-51.data.aliyun.com";
	    String path = "/rest/160601/ocr/ocr_idcard.json";
	    String method = "POST";
	    String appcode = "33d7bda16b6f465da90e598a33780602";
	    Map<String, String> headers = new HashMap<String, String>();
	    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
	    headers.put("Authorization", "APPCODE " + appcode);
	    //根据API的要求，定义相对应的Content-Type
	    headers.put("Content-Type", "application/json; charset=UTF-8");
	    Map<String, String> querys = new HashMap<String, String>();
	   
	    String pathImage = "C:/Users/z/Desktop/reverse.jpg"; 
	    //String pathImage = picPath; 
        String binaryToString = HttpUtils.getImageBinaryToString(picPath);  
		
	   
	    String bodys = "{\"inputs\": [{\"image\": {\"dataType\": 50,\"dataValue\": \"" + binaryToString  
                + "\"},\"configure\": {\"dataType\": 50,\"dataValue\": \"{\\\"side\\\":\\\"back\\\"}\"}}]}"; 
  
        try {
	    	
	    	HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
	    	System.out.println(response.toString());
	    	//获取response的body
//	    	String res =  EntityUtils.toString(response.getEntity());
//	    	JSONObject resultObj =  JSONObject.fromObject(res); 
	    	JSONObject resultObj = new JSONObject(EntityUtils.toString(response.getEntity()));  
            JSONArray outputArray = resultObj.getJSONArray("outputs");  
            String output = outputArray.getJSONObject(0).getJSONObject("outputValue").getString("dataValue"); // 取出结果json字符串  
            JSONObject out = new JSONObject(output);  
            
            if (out.getBoolean("success")) {  
                String end_date = out.getString("end_date"); // 获取有效截止日期
                map.put("end_date", end_date);
                String issue = out.getString("issue"); // 获取发证公安局 
                map.put("issue", issue);
                String start_date = out.getString("start_date"); // 获取生效日期  
                map.put("start_date", start_date);
               
                System.out.printf(" end_date : %s \n issue : %s \n start_date : %s ", end_date, issue,start_date);  
            } else {  
                System.out.println("predict error");  
            }  
	    	 
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		return map;
}
	
}
