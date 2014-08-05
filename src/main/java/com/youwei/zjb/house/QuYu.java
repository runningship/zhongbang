package com.youwei.zjb.house;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum QuYu {
	庐阳区("L"),
	瑶海区("Y"),
	新站区("X"),
	蜀山区("S"),
	包河区("B"),
	经开区("J"),
	政务区("Z"),
	高新区("G"),
	滨湖新区("BH"),
	肥东县("FD"),
	肥西县("FX"),
	长丰县("CF"),
	庐江县("LJ"),
	巢湖市("CH");
	
	public String shortPY;
	
	private QuYu(String py){
		this.shortPY = py;
	}
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(QuYu cx : QuYu.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("name", cx.name());
			jobj.put("value", cx.name());
			arr.add(jobj);
		}
		return arr;
	}
}
