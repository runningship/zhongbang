package com.youwei.zjb.work;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum PiYue {

	待批阅(0),
	已完成(1);
	
	private int code;
	
	private PiYue( int code){
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(PiYue sh : PiYue.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("value", sh.name());
			jobj.put("name", sh.name());
			jobj.put("code", sh.code);
			arr.add(jobj);
		}
		return arr;
	}
}
