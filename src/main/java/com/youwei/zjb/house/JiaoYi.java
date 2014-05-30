package com.youwei.zjb.house;

public enum JiaoYi {

	出售(1),
	出租(2),
	租售(3),
	仅售(4),
	仅租(5);
	
	private int code;
	
	private JiaoYi( int code){
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
}
