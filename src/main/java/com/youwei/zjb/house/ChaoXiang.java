package com.youwei.zjb.house;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum ChaoXiang {
	南,
	北,
	东,
	西,
	南北,
	东西,
	东南,
	西南,
	东北,
	西北;
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(ChaoXiang cx : ChaoXiang.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("name", cx.toString());
			jobj.put("value", cx.toString());
			arr.add(jobj);
		}
		return arr;
	}
}
