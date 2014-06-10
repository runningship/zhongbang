package com.youwei.zjb.house;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum LouXing {

	多层,
	高层,
	小高层,
	多层复式,
	高层复式,
	多层跃式,
	高层跃式,
	独立别墅,
	连体别墅,
	双拼别墅,
	裙楼,
	四合院,
	复式;
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(LouXing lx : LouXing.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("name", lx.toString());
			jobj.put("value", lx.toString());
			arr.add(jobj);
		}
		return arr;
	}
}
