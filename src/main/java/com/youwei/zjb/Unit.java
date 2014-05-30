package com.youwei.zjb;

public enum Unit{
	g("克"),
	mg("毫克"),
	ug("微克"),
	KJ("千焦");
	
	public   String   desc;
	
	private Unit(String desc){
		this.desc = desc;
	}
	
	
}
