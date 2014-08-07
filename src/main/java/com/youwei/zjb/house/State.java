package com.youwei.zjb.house;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum State {

	有效(1),
	无效(4),
	暂缓(3),
	我售(8),
	已售(7),
	我租(9),
	已租(10);
	private int code;
	
	private State( int code){
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(State state : State.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("value", state.name());
			jobj.put("name", state.name());
			jobj.put("code", state.code);
			arr.add(jobj);
		}
		return arr;
	}
	
	public static State parse(String code){
		int c = Integer.parseInt(code);
		for(State st : State.values()){
			if(st.code==c){
				return st;
			}
		}
		return null;
	}
}
