package com.youwei.zjb.client;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum KeHuLaiYuan {
	上门,
	网络,
	报纸,
	转介绍,
	外贴,
	其他;
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(KeHuLaiYuan cx : KeHuLaiYuan.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("name", cx.toString());
			jobj.put("value", cx.toString());
			arr.add(jobj);
		}
		return arr;
	}
}
