package com.github.catvod.spider;

import android.content.Context;
import android.util.Base64;

import com.github.catvod.bean.Class;
import com.github.catvod.bean.Result;
import com.github.catvod.bean.Vod;
import com.github.catvod.crawler.Spider;
import com.github.catvod.crawler.SpiderDebug;
import com.github.catvod.net.OkHttp;
import com.github.catvod.utils.Utils;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Xiaocge
 */
public class SC1080 extends Spider {

    private static String siteUrl = "https://sc1080.top";
    private JSONObject playerConfig;

    private HashMap<String, String> getHeaders() {
        HashMap<String, String> header = new HashMap<>();
        header.put("User-Agent", Utils.CHROME);
        return header;
    }

    @Override
    public void init(Context context, String extend) throws Exception {
        if (!extend.isEmpty())
            siteUrl = extend;
        try {
            playerConfig = getPlayerConfig();
        } catch (JSONException e) {
            SpiderDebug.log(e);
        }
    }

    private JSONObject getPlayerConfig() throws JSONException {
        Calendar calendar = Calendar.getInstance();
        String dateString = new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
        String format = String.format("%s/static/js/playerconfig.js?t=%s", siteUrl, dateString);
        String configJs = OkHttp.string(format);
        JSONObject jsonObject = new JSONObject(
                StringUtils.substringBetween(configJs, "MacPlayerConfig.player_list=", ",MacPlayerConfig"));

        return jsonObject;
    }

    @Override
    public String homeContent(boolean filter) throws Exception {
        List<Class> classes = new ArrayList<>();
        List<String> typeIds = Arrays.asList("1", "2", "3", "4", "5", "20");
        List<String> typeNames = Arrays.asList("电影", "连续剧", "综艺", "动漫", "纪录片", "Netflix");
        for (int i = 0; i < typeIds.size(); i++)
            classes.add(new Class(typeIds.get(i), typeNames.get(i)));
        String f = "{\"1\": [{\"key\": \"class\", \"name\": \"剧情\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"喜剧\", \"v\": \"class/喜剧\"}, {\"n\": \"爱情\", \"v\": \"class/爱情\"}, {\"n\": \"恐怖\", \"v\": \"恐怖\"}, {\"n\": \"动作\", \"v\": \"class/动作\"}, {\"n\": \"科幻\", \"v\": \"class/科幻\"}, {\"n\": \"剧情\", \"v\": \"class/剧情\"}, {\"n\": \"战争\", \"v\": \"class/战争\"}, {\"n\": \"警匪\", \"v\": \"class/警匪\"}, {\"n\": \"犯罪\", \"v\": \"class/犯罪\"}, {\"n\": \"动画\", \"v\": \"class/动画\"}, {\"n\": \"奇幻\", \"v\": \"class/奇幻\"}, {\"n\": \"武侠\", \"v\": \"class/武侠\"}, {\"n\": \"冒险\", \"v\": \"class/冒险\"}, {\"n\": \"枪战\", \"v\": \"class/枪战\"}, {\"n\": \"恐怖\", \"v\": \"class/恐怖\"}, {\"n\": \"悬疑\", \"v\": \"class/悬疑\"}, {\"n\": \"惊悚\", \"v\": \"class/惊悚\"}, {\"n\": \"经典\", \"v\": \"class/经典\"}, {\"n\": \"青春\", \"v\": \"class/青春\"}, {\"n\": \"文艺\", \"v\": \"class/文艺\"}, {\"n\": \"微电影\", \"v\": \"class/微电影\"}, {\"n\": \"古装\", \"v\": \"class/古装\"}, {\"n\": \"历史\", \"v\": \"class/历史\"}, {\"n\": \"运动\", \"v\": \"class/运动\"}, {\"n\": \"农村\", \"v\": \"class/农村\"}, {\"n\": \"儿童\", \"v\": \"class/儿童\"}, {\"n\": \"网络电影\", \"v\": \"class/网络电影\"}]}, {\"key\": \"area\", \"name\": \"地区\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"大陆\", \"v\": \"area/大陆\"}, {\"n\": \"香港\", \"v\": \"area/香港\"}, {\"n\": \"台湾\", \"v\": \"area/台湾\"}, {\"n\": \"美国\", \"v\": \"area/美国\"}, {\"n\": \"日本\", \"v\": \"area/日本\"}, {\"n\": \"韩国\", \"v\": \"area/韩国\"}, {\"n\": \"泰国\", \"v\": \"area/泰国\"}, {\"n\": \"德国\", \"v\": \"area/德国\"},  {\"n\": \"印度\", \"v\": \"area/印度\"},  {\"n\": \"意大利\", \"v\": \"area/意大利\"},  {\"n\": \"俄罗斯\", \"v\": \"area/俄罗斯\"}, {\"n\": \"西班牙\", \"v\": \"area/西班牙\"}, {\"n\": \"加拿大\", \"v\": \"area/加拿大\"}, {\"n\": \"其他\", \"v\": \"area/其他\"}]}, {\"key\": \"year\", \"name\": \"年份\", \"value\": [{\"n\": \"全部\", \"v\": \"\"},{\"n\": \"2024\", \"v\": \"year/2024\"},  {\"n\": \"2022\", \"v\": \"year/2022\"}, {\"n\": \"2021\", \"v\": \"year/2021\"}, {\"n\": \"2020\", \"v\": \"year/2020\"}, {\"n\": \"2019\", \"v\": \"year/2019\"}, {\"n\": \"2018\", \"v\": \"year/2018\"}, {\"n\": \"2017\", \"v\": \"year/2017\"}, {\"n\": \"2016\", \"v\": \"year/2016\"}, {\"n\": \"2015\", \"v\": \"year/2015\"}, {\"n\": \"2014\", \"v\": \"year/2014\"}, {\"n\": \"2013\", \"v\": \"year/2013\"}, {\"n\": \"2012\", \"v\": \"year/2012\"}, {\"n\": \"2011\", \"v\": \"year/2011\"}, {\"n\": \"2010\", \"v\": \"year/2010\"}]}, {\"key\": \"by\", \"name\": \"排序\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"时间\", \"v\": \"by/time\"}, {\"n\": \"人气\", \"v\": \"by/hits\"}, {\"n\": \"评分\", \"v\": \"by/score\"}]}],\"2\": [{\"key\": \"class\", \"name\": \"剧情\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"古装\", \"v\": \"class/古装\"}, {\"n\": \"战争\", \"v\": \"class/战争\"}, {\"n\": \"青春偶像\", \"v\": \"class/青春偶像\"}, {\"n\": \"喜剧\", \"v\": \"class/喜剧\"}, {\"n\": \"家庭\", \"v\": \"class/家庭\"}, {\"n\": \"犯罪\", \"v\": \"class/犯罪\"}, {\"n\": \"动作\", \"v\": \"class/动作\"}, {\"n\": \"奇幻\", \"v\": \"class/奇幻\"}, {\"n\": \"剧情\", \"v\": \"class/剧情\"}, {\"n\": \"历史\", \"v\": \"class/历史\"}, {\"n\": \"经典\", \"v\": \"class/经典\"}, {\"n\": \"乡村\", \"v\": \"class/乡村\"}, {\"n\": \"情景\", \"v\": \"class/情景\"}, {\"n\": \"商战\", \"v\": \"class/商战\"}, {\"n\": \"网剧\", \"v\": \"class/网剧\"}, {\"n\": \"其他\", \"v\": \"class/其他\"}]}, {\"key\": \"area\", \"name\": \"地区\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"大陆\", \"v\": \"area/大陆\"}, {\"n\": \"香港\", \"v\": \"area/香港\"}, {\"n\": \"台湾\", \"v\": \"area/台湾\"}, {\"n\": \"美国\", \"v\": \"area/美国\"}, {\"n\": \"日本\", \"v\": \"area/日本\"}, {\"n\": \"韩国\", \"v\": \"area/韩国\"},  {\"n\": \"泰国\", \"v\": \"area/泰国\"}, {\"n\": \"德国\", \"v\": \"area/德国\"},  {\"n\": \"印度\", \"v\": \"area/印度\"},  {\"n\": \"意大利\", \"v\": \"area/意大利\"},  {\"n\": \"俄罗斯\", \"v\": \"area/俄罗斯\"}, {\"n\": \"西班牙\", \"v\": \"area/西班牙\"}, {\"n\": \"加拿大\", \"v\": \"area/加拿大\"}, {\"n\": \"其他\", \"v\": \"area/其他\"}]}, {\"key\": \"year\", \"name\": \"年份\", \"value\": [{\"n\": \"全部\", \"v\": \"\"},{\"n\": \"2024\", \"v\": \"year/2024\"},  {\"n\": \"2022\", \"v\": \"year/2022\"}, {\"n\": \"2021\", \"v\": \"year/2021\"}, {\"n\": \"2020\", \"v\": \"year/2020\"}, {\"n\": \"2019\", \"v\": \"year/2019\"}, {\"n\": \"2018\", \"v\": \"year/2018\"}, {\"n\": \"2017\", \"v\": \"year/2017\"}, {\"n\": \"2016\", \"v\": \"year/2016\"}, {\"n\": \"2015\", \"v\": \"year/2015\"}, {\"n\": \"2014\", \"v\": \"year/2014\"}, {\"n\": \"2013\", \"v\": \"year/2013\"}]}, {\"key\": \"by\", \"name\": \"排序\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"时间\", \"v\": \"by/time\"}, {\"n\": \"人气\", \"v\": \"by/hits\"}, {\"n\": \"评分\", \"v\": \"by/score\"}]}], \"3\": [{\"key\": \"class\", \"name\": \"剧情\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"选秀\", \"v\": \"class/选秀\"}, {\"n\": \"情感\", \"v\": \"class/情感\"}, {\"n\": \"访谈\", \"v\": \"class/访谈\"}, {\"n\": \"播报\", \"v\": \"class/播报\"}, {\"n\": \"旅游\", \"v\": \"class/旅游\"}, {\"n\": \"音乐\", \"v\": \"class/音乐\"}, {\"n\": \"美食\", \"v\": \"class/美食\"}, {\"n\": \"纪实\", \"v\": \"class/纪实\"}, {\"n\": \"曲艺\", \"v\": \"class/曲艺\"}, {\"n\": \"生活\", \"v\": \"class/生活\"}, {\"n\": \"游戏\", \"v\": \"class/游戏\"}, {\"n\": \"财经\", \"v\": \"class/财经\"}, {\"n\": \"求职\", \"v\": \"class/求职\"}, {\"n\": \"其他\", \"v\": \"class/其他\"}]}, {\"key\": \"area\", \"name\": \"地区\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"大陆\", \"v\": \"area/大陆\"}, {\"n\": \"香港\", \"v\": \"area/香港\"}, {\"n\": \"台湾\", \"v\": \"area/台湾\"}, {\"n\": \"美国\", \"v\": \"area/美国\"}, {\"n\": \"日本\", \"v\": \"area/日本\"}, {\"n\": \"韩国\", \"v\": \"area/韩国\"}, {\"n\": \"其他\", \"v\": \"area/其他\"}]}, {\"key\": \"year\", \"name\": \"年份\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"2024\", \"v\": \"year/2024\"},  {\"n\": \"2023\", \"v\": \"year/2023\"}, {\"n\": \"2022\", \"v\": \"year/2022\"}, {\"n\": \"2021\", \"v\": \"year/2021\"}, {\"n\": \"2020\", \"v\": \"year/2020\"}, {\"n\": \"2019\", \"v\": \"year/2019\"}, {\"n\": \"2018\", \"v\": \"year/2018\"}, {\"n\": \"2017\", \"v\": \"year/2017\"}, {\"n\": \"2016\", \"v\": \"year/2016\"}, {\"n\": \"2015\", \"v\": \"year/2015\"}, {\"n\": \"2014\", \"v\": \"year/2014\"}, {\"n\": \"2013\", \"v\": \"year/2013\"}]}, {\"key\": \"by\", \"name\": \"排序\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"时间\", \"v\": \"by/time\"}, {\"n\": \"人气\", \"v\": \"by/hits\"}, {\"n\": \"评分\", \"v\": \"by/score\"}]}],\"4\": [{\"key\": \"class\", \"name\": \"剧情\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"情感\", \"v\": \"class/情感\"}, {\"n\": \"科幻\", \"v\": \"class/科幻\"}, {\"n\": \"热血\", \"v\": \"class/热血\"}, {\"n\": \"推理\", \"v\": \"class/推理\"}, {\"n\": \"搞笑\", \"v\": \"class/搞笑\"}, {\"n\": \"冒险\", \"v\": \"class/冒险\"}, {\"n\": \"萝莉\", \"v\": \"class/萝莉\"}, {\"n\": \"校园\", \"v\": \"class/校园\"}, {\"n\": \"动作\", \"v\": \"class/动作\"}, {\"n\": \"机战\", \"v\": \"class/机战\"}, {\"n\": \"运动\", \"v\": \"class/运动\"}, {\"n\": \"战争\", \"v\": \"class/战争\"}, {\"n\": \"少年\", \"v\": \"class/少年\"}, {\"n\": \"少女\", \"v\": \"class/少女\"}, {\"n\": \"社会\", \"v\": \"class/社会\"}, {\"n\": \"原创\", \"v\": \"class/原创\"}, {\"n\": \"亲子\", \"v\": \"class/亲子\"}, {\"n\": \"益智\", \"v\": \"class/益智\"}, {\"n\": \"励志\", \"v\": \"class/励志\"}, {\"n\": \"其他\", \"v\": \"class/其他\"}]}, {\"key\": \"area\", \"name\": \"地区\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"大陆\", \"v\": \"area/大陆\"}, {\"n\": \"香港\", \"v\": \"area/香港\"}, {\"n\": \"台湾\", \"v\": \"area/台湾\"}, {\"n\": \"美国\", \"v\": \"area/美国\"}, {\"n\": \"日本\", \"v\": \"area/日本\"}, {\"n\": \"韩国\", \"v\": \"area/韩国\"}, {\"n\": \"其他\", \"v\": \"area/其他\"}]}, {\"key\": \"year\", \"name\": \"年份\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"2024\", \"v\": \"year/2024\"},  {\"n\": \"2023\", \"v\": \"year/2023\"}, {\"n\": \"2022\", \"v\": \"year/2022\"}, {\"n\": \"2021\", \"v\": \"year/2021\"}, {\"n\": \"2020\", \"v\": \"year/2020\"}, {\"n\": \"2019\", \"v\": \"year/2019\"}, {\"n\": \"2018\", \"v\": \"year/2018\"}, {\"n\": \"2017\", \"v\": \"year/2017\"}, {\"n\": \"2016\", \"v\": \"year/2016\"}, {\"n\": \"2015\", \"v\": \"year/2015\"}, {\"n\": \"2014\", \"v\": \"year/2014\"}, {\"n\": \"2013\", \"v\": \"year/2013\"}]}, {\"key\": \"by\", \"name\": \"排序\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"时间\", \"v\": \"by/time\"}, {\"n\": \"人气\", \"v\": \"by/hits\"}, {\"n\": \"评分\", \"v\": \"by/score\"}]}],\"5\": [{\"key\": \"year\", \"name\": \"年份\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"2024\", \"v\": \"year/2024\"},  {\"n\": \"2023\", \"v\": \"year/2023\"}, {\"n\": \"2022\", \"v\": \"year/2022\"}, {\"n\": \"2021\", \"v\": \"year/2021\"}, {\"n\": \"2020\", \"v\": \"year/2020\"}, {\"n\": \"2019\", \"v\": \"year/2019\"}, {\"n\": \"2018\", \"v\": \"year/2018\"}, {\"n\": \"2017\", \"v\": \"year/2017\"}, {\"n\": \"2016\", \"v\": \"year/2016\"}, {\"n\": \"2015\", \"v\": \"year/2015\"}, {\"n\": \"2014\", \"v\": \"year/2014\"}, {\"n\": \"2013\", \"v\": \"year/2013\"}]}, {\"key\": \"by\", \"name\": \"排序\", \"value\": [{\"n\": \"全部\", \"v\": \"\"}, {\"n\": \"时间\", \"v\": \"by/time\"}, {\"n\": \"人气\", \"v\": \"by/hits\"}, {\"n\": \"评分\", \"v\": \"by/score\"}]}]}";
        JSONObject filterConfig = new JSONObject(f);
        Document doc = Jsoup.parse(OkHttp.string(siteUrl, getHeaders()));
        List<Vod> list = new ArrayList<>();
        for (Element li : doc.select(".module-item")) {
            if (list.size() > 30) {
                break;
            }
            String vid = siteUrl + li.select("a").attr("href");
            String name = li.select("a").attr("title");
            String pic = li.select("img").attr("data-src");
            String remark = li.select(".module-item-text").text();
            list.add(new Vod(vid, name, pic, remark));
        }
        return Result.string(classes, list, filterConfig);
    }

    @Override
    public String categoryContent(String tid, String pg, boolean filter, HashMap<String, String> extend)
            throws Exception {
        String cateId = extend.get("cateId") == null ? tid : extend.get("cateId");
        String area = extend.get("area") == null ? "" : extend.get("area");
        String year = extend.get("year") == null ? "" : extend.get("year");
        String by = extend.get("by") == null ? "" : extend.get("by");
        String classType = extend.get("class") == null ? "" : extend.get("class");
        String cateUrl = siteUrl + String.format("/index.php/vod/show/%s/%s/%s/id/%s/%s/page/%s.html", area, by,
                classType, cateId, year, pg);
        Document doc = Jsoup.parse(OkHttp.string(cateUrl, getHeaders()));
        List<Vod> list = new ArrayList<>();
        for (Element li : doc.select(".module-item")) {
            String vid = siteUrl + li.select("a").attr("href");
            String name = li.select("a").attr("title");
            String pic = li.select("img").attr("data-src");
            String remark = li.select(".module-item-text").text();
            list.add(new Vod(vid, name, pic, remark));
        }
        return Result.string(list);
    }

    @Override
    public String detailContent(List<String> ids) throws Exception {
        Document doc = Jsoup.parse(OkHttp.string(ids.get(0), getHeaders()));
        Elements sources = doc.select(".module-player-list .scroll-content"); // 线路
        Elements circuits = doc.select(".module-tab-item");// 播放源标题
        StringBuilder vod_play_url = new StringBuilder(); // 线路/播放源
        StringBuilder vod_play_from = new StringBuilder(); // 线路 / 播放源标题
        for (int i = 0; i < sources.size(); i++) {
            String spanText = circuits.get(i).select("span").text();
            if (spanText.contains("APP专线") || spanText.contains("Seven") || spanText.contains("蓝光B") || spanText.contains("蓝光C") || spanText.contains("蓝光M") || spanText.contains("蓝光T") || spanText.contains("蓝光P") || spanText.contains("腾讯视频") || spanText.contains("优酷视频") || spanText.contains("芒果TV") || spanText.contains("爱奇艺")) {
                continue;
            }
            String smallText = circuits.get(i).select("small").text();
            String playFromText = spanText + "(共" + smallText + "集)";
            vod_play_from.append(playFromText).append("$$$");
            Elements aElementArray = sources.get(i).select("div.scroll-content a");
            for (int j = 0; j < aElementArray.size(); j++) {
                Element a = aElementArray.get(j);
                String href = a.attr("href");
                String text = a.text();
                vod_play_url.append(text).append("$").append(href);
                boolean notLastEpisode = j < aElementArray.size() - 1;
                vod_play_url.append(notLastEpisode ? "#" : "$$$");
            }
        }

        String title = doc.select(".page-title").text();
        String pic = doc.select(".module-item-pic a").attr("title");
        String year = doc.select(".video-info-item").get(2).text();
        String remark = doc.select(".module-item-text").get(4).text();
        String actor = doc.select(".video-info-item").get(1).select("a").text();
        String director = doc.select(".video-info-item").get(0).select("a").text();
        String brief = doc.select(".video-info-content").text().trim();
        Vod vod = new Vod();
        vod.setVodId(ids.get(0));
        vod.setVodName(title);
        vod.setVodPic(pic);
        vod.setVodYear(year);
        vod.setVodDirector(director);
        vod.setVodActor(actor);
        vod.setVodRemarks(remark);
        vod.setVodContent(brief);
        vod.setVodPlayFrom(vod_play_from.toString());
        vod.setVodPlayUrl(vod_play_url.toString());
        return Result.string(vod);
    }

    public String playerContent(String flag, String id, List<String> vipFlags) throws Exception {
        try{
            String html = OkHttp.string(siteUrl + id, getHeaders());
            String regex = "var player_aaaa=([^<]+)</script>";
            Matcher matcher = Pattern.compile(regex).matcher(html);
            if (!matcher.find()) {
                return Result.error("出错, 稍后再试");
            }
            String group = matcher.group(1);
            JSONObject player = new JSONObject(group);
            String url = player.getString("url");
            url = new String(Base64.decode(url, Base64.DEFAULT | Base64.URL_SAFE | Base64.NO_WRAP), StandardCharsets.UTF_8);
            url = URLDecoder.decode(url);
            String from = player.getString("from");
            JSONObject playerObjConfig = playerConfig.getJSONObject(from);
            String matchUrl = playerObjConfig.getString("parse");
            String secUrl = matchUrl + url;
            String html2 = OkHttp.string(secUrl, getHeaders());

            Pattern pattern = Pattern.compile("var config = (\\{[^;]+\\})");
            Matcher matcher2 = pattern.matcher(html2);
            if (!matcher2.find()) {
                return "";
            }
            String group2 = matcher2.group(1);
            int lastIndex = group2.lastIndexOf(",");
            group2 = group2.substring(0, lastIndex) + group2.substring(lastIndex + 1);
            JSONObject playerConfig = new JSONObject(group2);

            HashMap<String, String> config = new HashMap<>();
            config.put("url", playerConfig.getString("url"));
            config.put("key", playerConfig.getString("key"));
            config.put("time", playerConfig.getString("time"));

            Pattern pattern3 = Pattern.compile("(https?://[^/]+)");
            Matcher matcher3 = pattern3.matcher(matchUrl);
            if (!matcher3.find()) {
                return "";
            }
            String domain = matcher3.group(1);
            String body = OkHttp.post(domain + "/api_config.php", config, getHeaders()).getBody();
            JSONObject jsonObject = new JSONObject(body);
            String realUrl = jsonObject.getString("url");
            HashMap<String, String> headers = getHeaders();
            headers.put("User-Agent", jsonObject.getString("user-agent"));

            return Result.get().url(realUrl).header(headers).string();
        }catch (Exception e) {
            return Result.get().url(siteUrl+id).parse().header(getHeaders()).string();
        }
    }

    @Override
    public String searchContent(String key, boolean quick) throws Exception {
        String searchUrl = siteUrl + "/index.php/vod/search.html?wd=" + URLEncoder.encode(key);
        Document doc = Jsoup.parse(OkHttp.string(searchUrl, getHeaders()));
        List<Vod> list = new ArrayList<>();
        for (Element li : doc.select(".module-search-item")) {
            String vid = siteUrl + li.select(".video-info-header > a").attr("href");
            String name = li.select("a").attr("title");
            String pic = li.select("img").attr("data-src");
            String remark = li.select(".module-item-text").text();
            list.add(new Vod(vid, name, pic, remark));
        }
        return Result.string(list);
    }
}
