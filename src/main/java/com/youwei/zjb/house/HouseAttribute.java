package com.youwei.zjb.house;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum HouseAttribute {

	私盘(7), 
	公盘( 6), 
	特盘(4), 
	封盘( 5),
	钥匙房(2);
    private int code;  
    // 构造方法  
    private HouseAttribute(int code) {  
        this.code = code;  
    }
	public int getCode() {
		return code;
	}  
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(HouseAttribute ha : HouseAttribute.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("value", ha.name());
			jobj.put("name", ha.name());
			jobj.put("code", ha.code);
			arr.add(jobj);
		}
		return arr;
	}
	
	public static HouseAttribute parse(String code){
		int c = Integer.parseInt(code);
		for(HouseAttribute st : HouseAttribute.values()){
			if(st.code==c){
				return st;
			}
		}
		return null;
	}
}
