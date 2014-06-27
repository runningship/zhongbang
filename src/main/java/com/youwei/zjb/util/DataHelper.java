package com.youwei.zjb.util;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class DataHelper {

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void fillDefaultValue(List<Map> list , String key,Object defValue){
		for(Map map : list){
			if(map.get(key)==null){
				map.put(key, defValue);
			}
		}
	}
	
	public static void escapeHtmlField(List<Map> list , String key){
		for(Map map : list){
			if(map.get(key)==null){
				continue;
			}
			Document doc = Jsoup.parse(map.get(key).toString());
			map.put(key, doc.text());
		}
	}
}
