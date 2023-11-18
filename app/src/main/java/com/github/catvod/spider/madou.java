package com.github.catvod.spider;

import android.content.Context;
import android.text.TextUtils;

import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.utils.okhttp.OkHttpUtil;

import java.net.URL;
import java.net.URLEncoder;
import java.net.URLConnection; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;  
import java.util.regex.Pattern;

import java.lang.String;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;
  
  
  

public class madou extends Spider {
    protected JSONObject ext = null;
    private static String api;
    @Override
    public void init(Context context, String extend) {
		super.init(context, extend);		
        api = extend;
    }

    protected HashMap<String, String> Headers() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 11; zh-CN; Redmi K30 Pro Build/RKQ1.200826.002) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/78.0.3904.108 Quark/5.1.2.182 Mobile Safari/537.36");
		headers.put("Connection", "Keep-Alive");
        return headers;
    }

    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend) {
		JSONObject dataObject = null;
		String html ="";
		String url = api + tid;
        try {            
            html = OkHttpUtil.string(url, Headers());
			int start = html.indexOf("class=\"box\"");  
            int end = html.indexOf("class=\"box\"", start + 11); //跳过第一个，寻找下面的box  
            String string = html.substring(start, end + 11 - start);  
             String string = html;  
            Pattern p1 = Pattern.compile("<a style\\=\\\"\" href\\=\\\"(.*?)\\\" title=\\\"(.*?)\\\">");  
            Pattern p2 = Pattern.compile("data-original\\=\\\"(.*?)\\\"");  
            Pattern p3 = Pattern.compile("<div class\\=\\\"duration\\\">(.*?)<\\/div>");  
              
            Matcher m1 = p1.matcher(string);  
            Matcher m2 = p2.matcher(string);  
            Matcher m3 = p3.matcher(string);  
              
            List<String> names = new ArrayList<>();  
            List<String> urls = new ArrayList<>();  
            List<String> pics = new ArrayList<>();  
            List<String> remarks = new ArrayList<>();  
              
            while (m1.find()) {  
                names.add(m1.group(2));  
                urls.add(m1.group(1));  
            }  
              
            while (m2.find()) {  
                pics.add(m2.group(1));  
            }  
              
            while (m3.find()) {  
                remarks.add(m3.group(1));  
            }
			dataObject = new JSONObject();
			// 创建一个新的列表来存储结果  
            List<Map<String, Object>> list = new ArrayList<>();  
              
            // 遍历数组 
            for (int i = 0; i < names.size(); i++) {    
                Map<String, Object> cla = new HashMap<>();    
                cla.put("name", names.get(i));    
                cla.put("url", urls.get(i));    
                cla.put("pic", pics.get(i));    
                cla.put("remarks", remarks.get(i));    
                list.add(cla);    
            }
			dataObject.put("list", list);
		}catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
		return dataObject.toString();	
	}

    @Override
    public String detailContent(List<String> ids) {
		String id = ids.get(0);
		JSONObject dataObject = null;
		try {
		    String name = id.split("$$$")[0];  
            if (name.contains("\\s+")){  
                name = name.split("\\s+")[1];  
            }  
            String url  = id.split("$$$")[1];  
              
            Map<String, Object> vod = new HashMap<>();  
            vod.put("vod_id", url);  
            vod.put("vod_name", name); //标题  
            vod.put("vod_pic", ""); //图片  
            vod.put("type_name", "未知"); //类型  
            vod.put("vod_year", "2023"); //年代  
            vod.put("vod_area", ""); //地区  
            vod.put("vod_remarks", "");  
            vod.put("vod_actor", "悟空"); //演员  
            vod.put("vod_director", getRandomDirector()); //导演  
            vod.put("vod_content", "随便搞点什么吧"); //简介  
            vod.put("vod_play_from", "madou");  
            vod.put("vod_play_url", api + url);
        	dataObject.put("list", vod);		
		}catch (Exception e10) {
            SpiderDebug.log(e10);
            return "";
        }
		return dataObject.toString();
    }    
	
    @Override
    public String homeContent(boolean filter) {
        JSONObject results = new JSONObject();
        try {
            String data = OkHttpUtil.string(api, Headers()); 
            //String start = extractBetween(data, "<strong>", "</strong>");  
		    Pattern pattern = Pattern.compile("<strong>(.*?)<\\/strong>");  
            Matcher matcher = pattern.matcher(data); 
			String extractedText = "";
            if (matcher.find()) {  
                extractedText = matcher.group(1);
			}				

            Pattern p = Pattern.compile("<a href\\=\\\"(.*?)\\\" class\\=\\\"(.*?)\\\">(.*?)<\\/a>");  
            Matcher m = p.matcher(extractedText);  
              
            ArrayList<String> urls = new ArrayList<String>();  
            ArrayList<String> names = new ArrayList<String>();  
            while (m.find()) {  
                urls.add(m.group(1));  
                names.add(m.group(3));  
            }    
            JSONArray classList = new JSONArray();  
            for (int i = 0; i < urls.size(); i++) {  
                JSONObject cla = new JSONObject();  
                cla.put("type_id" ,urls.get(i));				
                cla.put("type_name",names.get(i));  
                classList.put(cla);  
            }
            results.put("class", classList);			
        } catch (JSONException e2) {
            SpiderDebug.log(e2);
        }
        return results.toString();
    }
	
	public static String getRandomDirector() {  
        ArrayList<String> myArray = new ArrayList<String>();  
        myArray.add("詹姆斯·卡梅隆");  
        myArray.add("宫崎骏");  
        myArray.add("蒂姆·波顿");  
        myArray.add("彼得·杰克逊");  
        myArray.add("雷德利·斯科特");  
        myArray.add("大卫·芬奇");  
        myArray.add("阿方索·卡隆");  
        myArray.add("奉俊昊");  
        myArray.add("韦斯·安德森");  
        myArray.add("吕克·贝松");  
        myArray.add("张艺谋");  
        myArray.add("陈凯歌");  
        myArray.add("徐克");  
        myArray.add("冯小刚");  
  
        Random random = new Random();  
        int randomIndex = random.nextInt(myArray.size());  
        String randomValue = myArray.get(randomIndex);  
  
        return randomValue;  
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

    @Override
    public String playerContent(String str, String str2, List<String> list) {
        JSONObject result = new JSONObject();
        try {
            result.put("parse", 1);
            result.put("url", str2);
            result.put("jx", "0");
            //result.put("playUrl", "");
        } catch (Exception e2) {
            SpiderDebug.log(e2);
        }
        return result.toString();
    }

    @Override
    public String searchContent(String wd, boolean filter) {
		String url = "https://api.xinlangapi.com/xinlangapi.php/provide/vod/from/xlm3u8/?ac=&wd=" + wd;
		String data ="";
		JSONObject dataObject = null;
		try {
		    data = OkHttpUtil.string(url, Headers());
		    dataObject = new JSONObject(data);                 
		} catch (Exception e) {
            SpiderDebug.log(e);
            return "";
        }
        return dataObject.toString();
    }
}
