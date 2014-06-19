package com.youwei.zjb.client;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum KeHuXingzhi {

	潜在客户(0),
	标准客户(1);
	
	private int code;
	
	private KeHuXingzhi( int code){
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(KeHuXingzhi sh : KeHuXingzhi.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("value", sh.name());
			jobj.put("name", sh.name());
			jobj.put("code", sh.code);
			arr.add(jobj);
		}
		return arr;
	}
}
