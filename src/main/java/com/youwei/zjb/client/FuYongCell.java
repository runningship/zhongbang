package com.youwei.zjb.client;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum FuYongCell {
	可协商,
	一次性,
	可按揭;
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(FuYongCell cx : FuYongCell.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("name", cx.toString());
			jobj.put("value", cx.toString());
			arr.add(jobj);
		}
		return arr;
	}
}
