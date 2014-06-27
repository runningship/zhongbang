package com.youwei.zjb.user;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum RuQiTuJin {

	市场招聘,
	熟人介绍,
	门店招聘,
	网络招聘,
	媒体招聘,
	其他;
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(RuQiTuJin lx : RuQiTuJin.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("name", lx.name());
			jobj.put("value", lx.name());
			arr.add(jobj);
		}
		return arr;
	}
}
