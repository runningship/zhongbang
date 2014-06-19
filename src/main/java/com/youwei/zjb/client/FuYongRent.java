package com.youwei.zjb.client;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum FuYongRent {
	月付,
	季付,
	半年付,
	年付;
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(FuYongRent fy : FuYongRent.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("name", fy.toString());
			jobj.put("value", fy.toString());
			arr.add(jobj);
		}
		return arr;
	}
}
