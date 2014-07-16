package com.youwei.zjb.house;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum QuYu {
	庐阳区,
	瑶海区,
	新站区,
	蜀山区,
	经开区,
	政务区,
	高新区,
	滨湖新区,
	肥东县,
	肥西县,
	长丰县,
	庐江县,
	巢湖市;
	
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
