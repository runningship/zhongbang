package com.youwei.zjb.client;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *贷款类型
 */
public enum DaiKuanType {

	不贷款,
	商代,
	市直,
	省直,
	市直组合,
	省直组合,
	门面;
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(DaiKuanType dk : DaiKuanType.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("name", dk.name());
			jobj.put("value", dk.name());
			arr.add(jobj);
		}
		return arr;
	}
}
