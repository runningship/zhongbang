package com.youwei.zjb.house;


public enum DateType {

	委托日期("dateweituo",1),
	最后跟进日("dategenjin",2),
	交房到期日("datejiaofang",3),
	首次录入日("dateadd",4),
	建房年代("dateyear",5);
	private int code;
	private String field;
	private DateType(String field, int code){
		this.field = field;
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public String getField() {
		return field;
	}
	
}
