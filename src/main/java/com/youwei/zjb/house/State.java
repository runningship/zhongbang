package com.youwei.zjb.house;

public enum State {

	有效(1),
	无效(4),
	暂缓(3);
	private int code;
	
	private State( int code){
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
