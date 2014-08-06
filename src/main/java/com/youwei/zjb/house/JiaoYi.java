package com.youwei.zjb.house;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum JiaoYi {

	出售(1),
	出租(2),
	租售(3),
	仅售(4),
	仅租(5);
	
	private int code;
	
	JiaoYi( int code){
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(JiaoYi jy : JiaoYi.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("value", jy.name());
			jobj.put("name", jy.name());
			jobj.put("code", jy.code);
			arr.add(jobj);
		}
		return arr;
	}
	
	public static JiaoYi valueOf(int code){
		for(JiaoYi jy : JiaoYi.values()){
			if(jy.code==code){
				return jy;
			}
		}
		return null;
	}
}
