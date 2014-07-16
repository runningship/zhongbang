package com.youwei.zjb.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.bc.sdak.Page;

public class JSONHelper {

	public static JSONObject toJSON(Page<?> page){
		return toJSON(page,"");
	}
	public static JSONObject toJSON(Page<?> page,String timeFormat){
		if(page==null){
			return new JSONObject();
		}
		JSONObject jobj = JSONObject.fromObject(page);
		jobj.remove("result");
		JSONArray arr = new JSONArray();
		JsonConfig cfg = new JsonConfig();
		cfg.setIgnorePublicFields(false);
		cfg.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor(timeFormat));
		cfg.registerJsonValueProcessor(Timestamp.class, new JsonDateValueProcessor(timeFormat));
		for(Object obj : page.getResult()){
			arr.add(JSONObject.fromObject(obj,cfg));
		}
		jobj.put("data", arr);
		return jobj;
	}
	
	public static JSONObject toJSON(Object obj){
		if(obj==null){
			return new JSONObject();
		}
		JsonConfig cfg = new JsonConfig();
		cfg.setIgnorePublicFields(false);
		cfg.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		cfg.registerJsonValueProcessor(Timestamp.class, new JsonDateValueProcessor());
		return JSONObject.fromObject(obj, cfg);
	}
	
	public static JSONArray toJSONArray(List<?> list){
		if(list==null){
			return new JSONArray();
		}
		JSONArray arr = new JSONArray();
		JsonConfig cfg = new JsonConfig();
		cfg.setIgnorePublicFields(false);
		cfg.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		cfg.registerJsonValueProcessor(Timestamp.class, new JsonDateValueProcessor());
		for(Object obj : list){
			if(obj instanceof Enum){
				arr.add(JSONArray.fromObject(obj,cfg));
			}else{
				arr.add(JSONObject.fromObject(obj,cfg));
			}
		}
		return arr;
	}
	
}
