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
		if(page==null){
			return new JSONObject();
		}
		JSONObject jobj = JSONObject.fromObject(page);
		jobj.remove("result");
		JSONArray arr = new JSONArray();
		JsonConfig cfg = new JsonConfig();
		cfg.setIgnorePublicFields(false);
		cfg.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());
		for(Object obj : page.getResult()){
			arr.add(JSONObject.fromObject(obj,cfg));
		}
		jobj.put("data", arr);
		return jobj;
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
			arr.add(JSONObject.fromObject(obj,cfg));
		}
		return arr;
	}
	
}
