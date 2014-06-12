package com.youwei.zjb.house;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *房屋类别
 */
public enum HouseType {

	住宅,
	商住,
	商铺,
	店面,
	写字楼,
	车位,
	厂房,
	其他;
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(HouseType ht : HouseType.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("name", ht.name());
			jobj.put("value", ht.name());
			arr.add(jobj);
		}
		return arr;
	}
}
