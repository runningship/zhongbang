package com.youwei.zjb.sys;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum OperatorType {

	登录记录(1),
	授权记录(2),
	权限记录(3),
	房源记录(4),
	客源记录(5),
	人事记录(6),
	后台记录(7);
	
	private int code;
	
	private OperatorType( int code){
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(OperatorType sh : OperatorType.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("value", sh.name());
			jobj.put("name", sh.name());
			jobj.put("code", sh.code);
			arr.add(jobj);
		}
		return arr;
	}
}
