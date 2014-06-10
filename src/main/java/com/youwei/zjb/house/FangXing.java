package com.youwei.zjb.house;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum FangXing {

	单间("单间"," hxf = 1 "),
	房1("1房"," hxf = 1 "),
	房2("2房"," hxf = 2 "),
	房3("3房"," hxf = 3 "),
	房4("4房"," hxf = 4 "),
	房5("5房"," hxf = 5 "),
	单间1房("单间+1房"," hxf=2 "),
	房1到2("1-2房"," hxf =1 or hxf = 2 "),
	房2到3("2-3房"," hxf = 2 or hxf = 3 "),
	房3到4("3-4房"," hxf = 3 or hxf = 4 "),
	房4到5("4-5房"," hxf = 4 or hxf = 5 "),
	房6("6房以上"," hxf >= 6 ");
	
	private String name;
	private String queryStr;
	
	private FangXing(String name, String queryStr){
		this.name = name;
		this.queryStr = queryStr;
	}

	public String getName() {
		return name;
	}

	public String getQueryStr() {
		return queryStr;
	}
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(FangXing fx : FangXing.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("name", fx.toString());
			jobj.put("value", fx.toString());
			arr.add(jobj);
		}
		return arr;
	}
}
