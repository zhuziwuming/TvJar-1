package com.github.catvod.spider;

import android.content.Context;
import android.text.TextUtils;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.utils.okhttp.OkHttpUtil;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.lang.String;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ZWO extends Ali {
    protected JSONObject ext = null;
    private static String api;
    @Override
    public void init(Context context, String extend){
		String host = extend.split("\\$\\$\\$")[0];
		String token = OkHttpUtil.string(extend.split("\\$\\$\\$")[1],Headers());
	    super.init(context, token);
        try {
	    api = host + "api.php/provide/vod/?ac=detail";
            ext = new JSONObject("{\"classes\":[{\"type_name\":\"电影\",\"type_id\":\"1\"},{\"type_name\":\"电视剧\",\"type_id\":\"2\"},{\"type_name\":\"综艺\",\"type_id\":\"3\"},{\"type_name\":\"动漫\",\"type_id\":\"4\"}],\"filter\":{\"1\":[{\"name\":\"地区\",\"value\":[{\"v\":\"\",\"n\":\"全部\"},{\"v\":\"大陆\",\"n\":\"大陆\"},{\"v\":\"香港\",\"n\":\"香港\"},{\"v\":\"韩国\",\"n\":\"韩国\"},{\"v\":\"美国\",\"n\":\"美国\"}],\"key\":\"area\"},{\"key\":\"year\",\"name\":\"年代\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"2023\",\"v\":\"2023\"},{\"n\":\"2022\",\"v\":\"2022\"},{\"n\":\"2021\",\"v\":\"2021\"},{\"n\":\"2020\",\"v\":\"2020\"},{\"n\":\"2019\",\"v\":\"2019\"},{\"n\":\"2018\",\"v\":\"2018\"},{\"n\":\"2017\",\"v\":\"2017\"},{\"n\":\"2016\",\"v\":\"2016\"},{\"n\":\"2015\",\"v\":\"2015\"},{\"n\":\"90年代\",\"v\":\"1990~1999\"},{\"n\":\"更早\",\"v\":\"1800~1989\"}]}],\"2\":[{\"name\":\"地区\",\"value\":[{\"v\":\"\",\"n\":\"全部\"},{\"v\":\"大陆\",\"n\":\"大陆\"},{\"v\":\"香港\",\"n\":\"香港\"},{\"v\":\"韩国\",\"n\":\"韩国\"},{\"v\":\"美国\",\"n\":\"美国\"}],\"key\":\"area\"},{\"key\":\"year\",\"name\":\"年代\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"2023\",\"v\":\"2023\"},{\"n\":\"2022\",\"v\":\"2022\"},{\"n\":\"2021\",\"v\":\"2021\"},{\"n\":\"2020\",\"v\":\"2020\"},{\"n\":\"2019\",\"v\":\"2019\"},{\"n\":\"2018\",\"v\":\"2018\"},{\"n\":\"2017\",\"v\":\"2017\"},{\"n\":\"2016\",\"v\":\"2016\"},{\"n\":\"2015\",\"v\":\"2015\"},{\"n\":\"90年代\",\"v\":\"1990~1999\"},{\"n\":\"更早\",\"v\":\"1800~1989\"}]}],\"3\":[{\"name\":\"地区\",\"value\":[{\"v\":\"\",\"n\":\"全部\"},{\"v\":\"大陆\",\"n\":\"大陆\"},{\"v\":\"香港\",\"n\":\"香港\"},{\"v\":\"韩国\",\"n\":\"韩国\"},{\"v\":\"美国\",\"n\":\"美国\"}],\"key\":\"area\"},{\"key\":\"year\",\"name\":\"年代\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"2023\",\"v\":\"2023\"},{\"n\":\"2022\",\"v\":\"2022\"},{\"n\":\"2021\",\"v\":\"2021\"},{\"n\":\"2020\",\"v\":\"2020\"},{\"n\":\"2019\",\"v\":\"2019\"},{\"n\":\"2018\",\"v\":\"2018\"},{\"n\":\"2017\",\"v\":\"2017\"},{\"n\":\"2016\",\"v\":\"2016\"},{\"n\":\"2015\",\"v\":\"2015\"},{\"n\":\"90年代\",\"v\":\"1990~1999\"},{\"n\":\"更早\",\"v\":\"1800~1989\"}]}],\"4\":[{\"name\":\"地区\",\"value\":[{\"v\":\"\",\"n\":\"全部\"},{\"v\":\"大陆\",\"n\":\"大陆\"},{\"v\":\"香港\",\"n\":\"香港\"},{\"v\":\"韩国\",\"n\":\"韩国\"},{\"v\":\"美国\",\"n\":\"美国\"}],\"key\":\"area\"},{\"key\":\"year\",\"name\":\"年代\",\"value\":[{\"n\":\"全部\",\"v\":\"\"},{\"n\":\"2023\",\"v\":\"2023\"},{\"n\":\"2022\",\"v\":\"2022\"},{\"n\":\"2021\",\"v\":\"2021\"},{\"n\":\"2020\",\"v\":\"2020\"},{\"n\":\"2019\",\"v\":\"2019\"},{\"n\":\"2018\",\"v\":\"2018\"},{\"n\":\"2017\",\"v\":\"2017\"},{\"n\":\"2016\",\"v\":\"2016\"},{\"n\":\"2015\",\"v\":\"2015\"},{\"n\":\"90年代\",\"v\":\"1990~1999\"},{\"n\":\"更早\",\"v\":\"1800~1989\"}]}]}}");
        } catch (Exception ignored) {
        }
    }

    protected HashMap<String, String> Headers() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.114 Safari/537.36");
		//headers.put("Referer","https://www.wogg.xyz/");		
        return headers;
    }

    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
		JSONObject dataObject = null;
		String data ="";
		String url = api + "&t=" + tid + "&pg=" + pg;
        try { 
            Set<String> keys = extend.keySet();
            for (String key : keys) {
                String val = extend.get(key).trim();
                if (val.length() == 0)
                    continue;
                url += "&" + key + "=" + URLEncoder.encode(val);
            }
            data = OkHttpUtil.string(url, Headers());
            dataObject = new JSONObject(data);
            JSONArray jsonArray = dataObject.getJSONArray("list");
            JSONArray videos = new JSONArray();
            for (int i = 0; i < jsonArray.length(); i++) {
		        JSONObject vObj = jsonArray.getJSONObject(i);
		        String down_url = vObj.getString("vod_down_url");
		        String vod_remarks = vObj.getString("vod_remarks");
		        if(down_url.contains("\\$\\$\\$")){
		        	down_url = down_url.split("\\$\\$\\$")[0];
		        }
		        if(down_url.contains("aliyundrive.com")||down_url.contains("alipan.com")){
		        	vObj.put("vod_id", down_url);	
		            vObj.put("vod_remarks", vod_remarks + "(VIP)");
		        }
                    videos.put(vObj);					
            }
            dataObject.put("list", videos);		
        } catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
		return dataObject.toString();	
    }

    // @Override
    // public String detailContent(List<String> ids) throws Exception {
		// try {
            // Pattern pattern = aliyun;
            // if (pattern.matcher(list.get(0)).find()) {
                // return pushAgent.detailContent(list);
            // }
        // } catch (Exception e) {
            // SpiderDebug.log(e);
        // }
        // return "";
    // }
	
    @Override
    public String homeContent(boolean filter) {
		String url =  api.replace("detail", "list");// xxx.com/api.php/provide/vod/?ac=list
        JSONObject results = new JSONObject();
		JSONArray listArray = new JSONArray();
		JSONObject dataObject = null;
		String data ="";
        try {			 
			data = OkHttpUtil.string(url, Headers());
            dataObject = new JSONObject(data);
			JSONArray classarr = dataObject.getJSONArray("class");
			List<JSONObject> newArray = new ArrayList<>();
            if (classarr.length() > 0) { //取大分类
                for (int i = 0; i < classarr.length(); i++) {  
                    JSONObject obj = classarr.getJSONObject(i);  
                    int typePid = obj.optInt("type_pid", -1); // 使用optInt以避免异常  
                    if (typePid == 0 || !obj.has("type_pid")) { // 修改条件顺序
                        newArray.add(obj);  
                    }  
                }
				JSONArray newClassArray = new JSONArray(newArray);
				results.put("class", newClassArray);
			}else{
				results.put("class", ext.getJSONObject("classes"));//取固定值
			}
			results.put("list", listArray);
            if (filter) {
                results.put("filters", ext.getJSONObject("filter"));
            }
        } catch (JSONException e2) {
            SpiderDebug.log(e2);
        }
        return results.toString();
    }
	

    @Override
    public String homeVideoContent() {
        JSONObject list = new JSONObject();
		String url = api;
		String data ="";
		JSONObject dataObject = null;
        try {
            data = OkHttpUtil.string(url, Headers());
            dataObject = new JSONObject(data);
			
        } catch (Exception e2) {
            SpiderDebug.log(e2);
			return "";
        }
        return dataObject.toString();        
    }

    // @Override
    // public String playerContent(String str, String str2, List<String> list) {
        // return pushAgent.playerContent(str, str2, list);
    // }

    @Override
    public String searchContent(String key, boolean quick) throws Exception {
	    String url = api+ "&wd=" + key;
	    String data ="";
	    JSONObject dataObject = null;
	    try {
	        data = OkHttpUtil.string(url, Headers());
	        dataObject = new JSONObject(data);
            JSONArray jsonArray = dataObject.getJSONArray("list");
            JSONArray videos = new JSONArray();
            for (int i = 0; i < jsonArray.length(); i++) {
	    	    JSONObject vObj = jsonArray.getJSONObject(i);
	    	    String down_url = vObj.getString("vod_down_url");
	    	    String vod_remarks = vObj.getString("vod_remarks");
	    	    if(down_url.contains("\\$\\$\\$")){
	    	       	down_url = down_url.split("\\$\\$\\$")[0];
	    	    }
	    	    if(down_url.contains("ali")){
	    	  	    vObj.put("vod_id", down_url);	
	    	        vObj.put("vod_remarks", vod_remarks + "(VIP)");
	    	    }
                videos.put(vObj);  // 将修改过的 video 数据放入新的 JSONArray中					
            }
            dataObject.put("list", videos);        
	    } catch (Exception e) {
                SpiderDebug.log(e);
                return "";
        }
        return dataObject.toString();
    }
}
