package com.youwei.zjb.house;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum ShenHe {

	未审核(0),
	已审核(1);
	
	private int code;
	
	private ShenHe( int code){
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(ShenHe sh : ShenHe.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("value", sh.name());
			jobj.put("name", sh.name());
			jobj.put("code", sh.code);
			arr.add(jobj);
		}
		return arr;
	}
}
