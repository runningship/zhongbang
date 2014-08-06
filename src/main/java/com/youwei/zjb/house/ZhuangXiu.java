package com.youwei.zjb.house;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum ZhuangXiu {

	毛坯(1),
	清水(2),
	简装(3),
	中装(4),
	精装(5),
	豪装(6);
	
	private int code;
	
	private ZhuangXiu( int code){
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(ZhuangXiu zx : ZhuangXiu.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("value", zx.name());
			jobj.put("name", zx.name());
			jobj.put("code", zx.code);
			arr.add(jobj);
		}
		return arr;
	}
	
	public static ZhuangXiu valueOf(int code){
		for(ZhuangXiu zx : ZhuangXiu.values()){
			if(zx.code==code){
				return zx;
			}
		}
		return null;
	}
}
